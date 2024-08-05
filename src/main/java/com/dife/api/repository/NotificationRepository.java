package com.dife.api.repository;

import com.dife.api.model.Notification;
import com.dife.api.model.NotificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	boolean existsByNotificationToken(NotificationToken notificationToken);
}
