package com.dife.api.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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
public class NotificationController implements SwaggerNotificationController {

	private final NotificationService notificationService;

	@GetMapping("/push")
	public ResponseEntity<List<NotificationTokenResponseDto>> getNotificationTokens(
			Authentication auth) {
		List<NotificationTokenResponseDto> responseDtos =
				notificationService.getNotificationTokens(auth.getName());
		return ResponseEntity.ok(responseDtos);
	}

	@GetMapping("/{deviceId}")
	public ResponseEntity<List<NotificationResponseDto>> getNotifications(
			@PathVariable("deviceId") String deviceId, Authentication auth) {
		List<NotificationResponseDto> responseDtos =
				notificationService.getNotifications(deviceId, auth.getName());
		return ResponseEntity.ok(responseDtos);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteNotification(@PathVariable("id") Long id, Authentication auth) {
		notificationService.deleteNotification(id, auth.getName());
		return new ResponseEntity<>(OK);
	}

	@PostMapping("/push")
	public ResponseEntity<NotificationTokenResponseDto> createNotificationToken(
			@RequestBody NotificationTokenRequestDto requestDto, Authentication auth) {
		NotificationTokenResponseDto responseDto =
				notificationService.sendNotificationToken(auth.getName(), requestDto);
		return ResponseEntity.status(CREATED).body(responseDto);
	}
}
