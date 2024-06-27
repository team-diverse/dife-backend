package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.exception.NotificationException;
import com.dife.api.model.Member;
import com.dife.api.model.Notification;
import com.dife.api.model.NotificationType;
import com.dife.api.model.dto.NotificationResponseDto;
import com.dife.api.repository.MemberRepository;
import com.dife.api.repository.NotificationRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationService {

	private final Map<Long, SseEmitter> memberEmitters = new ConcurrentHashMap<>();
	private static final long RECONNECTION_TIMEOUT = 1000L;
	private final ModelMapper modelMapper;

	private final MemberRepository memberRepository;
	private final NotificationRepository notificationRepository;

	public SseEmitter createEmitter(String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
		memberEmitters.put(member.getId(), emitter);

		emitter.onCompletion(() -> memberEmitters.remove(member.getId()));
		emitter.onTimeout(() -> memberEmitters.remove(member.getId()));
		emitter.onError((e) -> memberEmitters.remove(member.getId()));

		try {
			SseEmitter.SseEventBuilder event =
					SseEmitter.event()
							.name("notification")
							.id(String.valueOf("id-1"))
							.data("SSE connected")
							.reconnectTime(RECONNECTION_TIMEOUT);
			emitter.send(event);
		} catch (IOException e) {
			throw new NotificationException();
		}

		return emitter;
	}

	public void sendNotification(String memberEmail, NotificationType type, String message) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Notification notification = new Notification();
		notification.setType(type);
		notification.setMessage(message);
		notification.setMember(member);
		notificationRepository.save(notification);

		sendRealTimeNotification(notification, member.getId());
	}

	private void sendRealTimeNotification(Notification notification, Long memberId) {
		SseEmitter emitter = memberEmitters.get(memberId);

		if (emitter != null) {
			Executors.newSingleThreadExecutor()
					.execute(
							() -> {
								try {
									emitter.send(
											SseEmitter.event().name("notification").data(notification.getMessage()));
								} catch (Exception e) {
									throw new NotificationException();
								}
							});
		}
	}

	public List<NotificationResponseDto> getNotifications(String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		List<Notification> notifications = notificationRepository.findAllByMember(member);

		return notifications.stream()
				.map(n -> modelMapper.map(n, NotificationResponseDto.class))
				.collect(toList());
	}
}
