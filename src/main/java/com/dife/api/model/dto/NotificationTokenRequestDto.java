package com.dife.api.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTokenRequestDto {

	private String pushToken;
	private String deviceId;
}
