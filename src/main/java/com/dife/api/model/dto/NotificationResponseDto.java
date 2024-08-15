package com.dife.api.model.dto;

import com.dife.api.model.NotificationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponseDto {

	private Long id;

	private NotificationType type;

	private Long typeId;

	private String chatMemberEmail;

	private String message;
}
