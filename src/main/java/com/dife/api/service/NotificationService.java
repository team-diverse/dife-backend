package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.exception.NotificationException;
import com.dife.api.model.Member;
import com.dife.api.model.Notification;
import com.dife.api.model.NotificationToken;
import com.dife.api.model.dto.NotificationRequestDto;
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

	public NotificationResponseDto sendNotification(
			String memberEmail, NotificationRequestDto requestDto) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		NotificationToken notificationToken =
				notificationTokenRepository.findByMember(member).orElseThrow(NotificationException::new);

		Notification notification = new Notification();
		notification.setNotificationToken(notificationToken);
		notification.setType(requestDto.getType());
		notification.setMessage(requestDto.getMessage());
		notification.setIsRead(requestDto.getIsRead());

		notificationToken.getNotifications().add(notification);

		notificationTokenRepository.save(notificationToken);

		return modelMapper.map(notification, NotificationResponseDto.class);
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
}
