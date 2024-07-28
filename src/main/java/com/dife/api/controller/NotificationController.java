package com.dife.api.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.dife.api.model.dto.NotificationRequestDto;
import com.dife.api.model.dto.NotificationResponseDto;
import com.dife.api.model.dto.NotificationTokenRequestDto;
import com.dife.api.model.dto.NotificationTokenResponseDto;
import com.dife.api.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController implements SwaggerNotificationController{

	private final NotificationService notificationService;

	@GetMapping("/push")
	public ResponseEntity<List<NotificationTokenResponseDto>> getNotificationTokens(Authentication auth) {
		List<NotificationTokenResponseDto> responseDtos =
				notificationService.getNotificationTokens(auth.getName());
		return ResponseEntity.ok(responseDtos);
	}

	@GetMapping
	public ResponseEntity<List<NotificationResponseDto>> getNotifications(Authentication auth) {
		List<NotificationResponseDto> responseDtos =
				notificationService.getNotifications(auth.getName());
		return ResponseEntity.ok(responseDtos);
	}

	@PostMapping("/push")
	public ResponseEntity<NotificationTokenResponseDto> createNotificationToken(
			@RequestBody NotificationTokenRequestDto requestDto, Authentication auth) {
		NotificationTokenResponseDto responseDto =
				notificationService.sendNotificationToken(auth.getName(), requestDto);
		return ResponseEntity.status(CREATED).body(responseDto);
	}

	@PostMapping("/send")
	public ResponseEntity<NotificationResponseDto> createNotification(
			@RequestBody NotificationRequestDto requestDto, Authentication auth) {
		NotificationResponseDto responseDto =
				notificationService.sendNotification(auth.getName(), requestDto);
		return ResponseEntity.status(CREATED).body(responseDto);
	}
}
