package com.dife.api.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "notificationToken")
public class NotificationToken extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String pushToken;

	private String deviceId;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "notificationToken", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Notification> notifications = new ArrayList<>();
}
