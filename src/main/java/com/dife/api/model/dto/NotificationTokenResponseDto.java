package com.dife.api.model.dto;

import com.dife.api.model.Notification;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationTokenResponseDto {

	private Long id;

	private String pushToken;

	private String deviceId;

	private MemberRestrictedResponseDto member;

	private List<Notification> notifications;
}
