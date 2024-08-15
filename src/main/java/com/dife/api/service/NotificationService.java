package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.exception.NotificationAuthorizationException;
import com.dife.api.exception.NotificationException;
import com.dife.api.model.Member;
import com.dife.api.model.Notification;
import com.dife.api.model.NotificationToken;
import com.dife.api.model.NotificationType;
import com.dife.api.model.dto.NotificationResponseDto;
import com.dife.api.model.dto.NotificationTokenRequestDto;
import com.dife.api.model.dto.NotificationTokenResponseDto;
import com.dife.api.repository.MemberRepository;
import com.dife.api.repository.NotificationRepository;
import com.dife.api.repository.NotificationTokenRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationService {

	private final ModelMapper modelMapper;

	private final MemberRepository memberRepository;
	private final NotificationTokenRepository notificationTokenRepository;
	private final NotificationRepository notificationRepository;

	public NotificationTokenResponseDto sendNotificationToken(
			String memberEmail, NotificationTokenRequestDto requestDto) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		NotificationToken notificationToken = new NotificationToken();
		notificationToken.setPushToken(requestDto.getPushToken());
		notificationToken.setDeviceId(requestDto.getDeviceId());
		notificationToken.setMember(member);

		member.getNotificationTokens().add(notificationToken);

		notificationTokenRepository.save(notificationToken);

		return modelMapper.map(notificationToken, NotificationTokenResponseDto.class);
	}

	public List<NotificationTokenResponseDto> getNotificationTokens(String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		List<NotificationToken> notifications = notificationTokenRepository.findAllByMember(member);

		return notifications.stream()
				.map(n -> modelMapper.map(n, NotificationTokenResponseDto.class))
				.collect(toList());
	}

	public List<NotificationResponseDto> getNotifications(String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		List<NotificationToken> notificationTokens =
				notificationTokenRepository.findAllByMember(member);

		return notificationTokens.stream()
				.flatMap(notificationToken -> notificationToken.getNotifications().stream())
				.map(notification -> modelMapper.map(notification, NotificationResponseDto.class))
				.collect(toList());
	}

	public void sendPushNotification(String expoToken, String message) {
		String expoPushUrl = "https://exp.host/--/api/v2/push/send";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, Object> body = new HashMap<>();
		body.put("to", expoToken);
		body.put("sound", "default");
		body.put("body", message);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response =
				restTemplate.exchange(expoPushUrl, HttpMethod.POST, entity, String.class);

		if (response.getStatusCode() != HttpStatus.OK) throw new NotificationException();
	}

	public void addNotifications(
			Member toMember,
			Member currentMember,
			String messageTemplate,
			NotificationType type,
			Long typeId) {
		if (!Objects.equals(toMember.getId(), currentMember.getId())
				&& type != NotificationType.CONNECT) {
			List<NotificationToken> notificationTokens = toMember.getNotificationTokens();

			for (NotificationToken notificationToken : notificationTokens) {
				Notification notification = new Notification();
				notification.setNotificationToken(notificationToken);
				notification.setType(type);
				notification.setTypeId(typeId);

				String message = String.format(messageTemplate, currentMember.getUsername());
				notification.setMessage(message);
				notificationToken.getNotifications().add(notification);

				sendPushNotification(notificationToken.getPushToken(), message);
			}
		}
	}

	public void deleteNotification(Long id, String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		List<NotificationToken> notifications = notificationTokenRepository.findAllByMember(member);

		for (NotificationToken notificationToken : notifications) {
			if (!notificationRepository.existsByNotificationToken(notificationToken))
				throw new NotificationAuthorizationException();
			for (Notification notification : notificationToken.getNotifications()) {
				if (Objects.equals(notification.getId(), id)) {
					notificationToken.getNotifications().remove(notification);
					notificationRepository.delete(notification);
					break;
				}
			}
		}
	}
}
