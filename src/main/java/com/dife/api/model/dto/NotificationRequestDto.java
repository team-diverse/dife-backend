package com.dife.api.model.dto;

import com.dife.api.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDto {

	private Long tokenId;
	private NotificationType type;
	private String message;
	private Boolean isRead = false;
}
