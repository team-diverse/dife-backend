package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.model.Member;
import com.dife.api.model.NotificationToken;
import com.dife.api.model.dto.NotificationResponseDto;
import com.dife.api.model.dto.NotificationTokenRequestDto;
import com.dife.api.model.dto.NotificationTokenResponseDto;
import com.dife.api.repository.MemberRepository;
import com.dife.api.repository.NotificationTokenRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationService {

	private final ModelMapper modelMapper;

	private final MemberRepository memberRepository;
	private final NotificationTokenRepository notificationTokenRepository;

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
		List<NotificationToken> notifications = notificationTokenRepository.findAllByMember(member);

		return notifications.stream()
				.map(n -> modelMapper.map(n, NotificationResponseDto.class))
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
}
