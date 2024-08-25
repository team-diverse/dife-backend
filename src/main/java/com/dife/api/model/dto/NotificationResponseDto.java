package com.dife.api.model.dto;

import com.dife.api.model.NotificationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponseDto {

	private Long id;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	private Long typeId;

	private String chatMemberEmail;

	private String message;

	private LocalDateTime created;
}
