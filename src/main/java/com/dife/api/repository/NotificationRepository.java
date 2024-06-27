package com.dife.api.repository;

import com.dife.api.model.Member;
import com.dife.api.model.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findAllByMember(Member member);
}
