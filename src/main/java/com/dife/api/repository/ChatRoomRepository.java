package com.dife.api.repository;

import com.dife.api.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    boolean existsByChatRoomName(String chatRoomName);
}
