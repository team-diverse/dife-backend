package com.dife.api.handler;

import com.dife.api.model.*;
import com.dife.api.service.NotificationService;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class NotificationHandler {

	private final NotificationService notificationService;

	public void isAlone(Chatroom chatroom, Member exitMember) {
		if (chatroom.getMembers().size() < 2 && chatroom.getChatroomType() == ChatroomType.GROUP)
			notificate(chatroom, exitMember);
	}

	private String translationDivide(Chatroom chatroom, String settingLanguage, Member exitMember) {

		String baseMessage = "📢 ";
		ResourceBundle resourceBundle;
		String chatroomName = chatroom.getName();
		baseMessage += "(IN CHATROOM, " + chatroomName + ")";
		resourceBundle =
				ResourceBundle.getBundle("notification.whenGroupChatroomAlone", Locale.getDefault());
		String messageSuffix = resourceBundle.getString(settingLanguage.toUpperCase());
		baseMessage += messageSuffix;
		return baseMessage;
	}

	public void notificate(Chatroom chatroom, Member exitMember) {

		Set<Member> members = chatroom.getMembers();
		Member member =
				members.stream()
						.findFirst()
						.orElseThrow(() -> new NoSuchElementException("Member not found"));

		String settingLanguage = member.getSettingLanguage();
		List<NotificationToken> notificationTokens = member.getNotificationTokens();

		String notificationMessage = translationDivide(chatroom, settingLanguage, exitMember);

		for (NotificationToken notificationToken : notificationTokens) {
			Notification notification = new Notification();
			notification.setNotificationToken(notificationToken);
			notification.setType(NotificationType.CHATROOM);
			notification.setCreated(LocalDateTime.now());
			notification.setMessage(notificationMessage);
			notificationToken.getNotifications().add(notification);

			notificationService.sendPushNotification(
					notificationToken.getPushToken(), notification.getCreated(), notificationMessage);
		}
	}
}
