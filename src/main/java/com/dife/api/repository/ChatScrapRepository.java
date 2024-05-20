package com.dife.api.repository;

import com.dife.api.model.ChatScrap;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatScrapRepository extends JpaRepository<ChatScrap, Long> {

	List<ChatScrap> findScrapsByChatroomId(Long room_id);

	Optional<ChatScrap> findByChatroomIdAndId(
			@Param("room_id") Long room_id, @Param("chatScrap_id") Long chatScrap_id);
}
