package com.dife.api.repository;

import com.dife.api.model.ChatroomSetting;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomSettingRepository extends JpaRepository<ChatroomSetting, Long> {

	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT s FROM ChatroomSetting s WHERE s.id = :id")
	ChatroomSetting findByIdWithPessimisticLock(Long id);

	@Lock(value = LockModeType.OPTIMISTIC)
	@Query("SELECT s FROM ChatroomSetting s WHERE s.id = :id")
	ChatroomSetting findByIdWithOptimisticLock(Long id);
}
