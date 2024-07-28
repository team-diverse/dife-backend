package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

	private NotificationType type;

	private String message;

	private Boolean isRead = false;

	@ManyToOne
	@JoinColumn(name = "notificationToken_id")
	@JsonIgnore
	private NotificationToken notificationToken;
}
