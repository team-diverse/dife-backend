package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "notification")
public class Notification extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	private Long typeId;

	private String chatMemberEmail;

	private String message;

	private LocalDateTime created;

	@ManyToOne
	@JoinColumn(name = "notification_token_id")
	@JsonIgnore
	private NotificationToken notificationToken;
}
