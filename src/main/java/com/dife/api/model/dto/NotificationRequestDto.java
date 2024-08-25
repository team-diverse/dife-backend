package com.dife.api.model.dto;

import com.dife.api.model.NotificationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	private String message;
	private Boolean isRead = false;
}
