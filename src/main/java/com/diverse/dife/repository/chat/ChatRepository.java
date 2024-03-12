package com.diverse.dife.repository.chat;

import com.diverse.dife.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
