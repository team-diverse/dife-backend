package com.dife.api.repository;

import com.dife.api.model.Chat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

	List<Chat> findChatsByChatroomId(Long chatroom_id);

	Optional<Chat> findByChatroomIdAndId(Long chatroom_id, Long id);
}
