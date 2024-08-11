package com.dife.api.repository;

import com.dife.api.model.Member;
import com.dife.api.model.NotificationToken;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Long> {

	List<NotificationToken> findAllByMember(Member member);
}
