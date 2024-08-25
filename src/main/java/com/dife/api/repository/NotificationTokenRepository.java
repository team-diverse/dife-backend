package com.dife.api.repository;

import com.dife.api.model.Member;
import com.dife.api.model.NotificationToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Long> {

	Optional<NotificationToken> findAllByMemberAndDeviceId(Member member, String deviceId);

	List<NotificationToken> findAllByMember(Member member);

	boolean existsByDeviceId(String deviceId);

	Optional<NotificationToken> findByDeviceId(String deviceId);
}
