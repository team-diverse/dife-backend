package com.dife.api.controller;

import com.dife.api.model.dto.NotificationResponseDto;
import com.dife.api.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter streamNotifications(Authentication auth) {
		return notificationService.createEmitter(auth.getName());
	}

	@GetMapping
	public ResponseEntity<List<NotificationResponseDto>> getNotifications(Authentication auth) {
		List<NotificationResponseDto> responseDtos =
				notificationService.getNotifications(auth.getName());
		return ResponseEntity.ok(responseDtos);
	}
}
