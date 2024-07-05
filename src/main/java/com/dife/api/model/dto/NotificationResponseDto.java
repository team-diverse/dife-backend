package com.dife.api.model.dto;

import com.dife.api.model.NotificationType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponseDto {

	private Long id;

	private NotificationType type;

	private String message;

	private Boolean isRead;

	private LocalDateTime created;
}
