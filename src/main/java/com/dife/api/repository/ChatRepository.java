package com.dife.api.repository;

import com.dife.api.model.Chat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat, Long> {

	List<Chat> findChatsByChatroomId(Long room_id);

	Optional<Chat> findByChatroomIdAndId(
			@Param("room_id") Long roomId, @Param("chat_id") Long chatId);
}
