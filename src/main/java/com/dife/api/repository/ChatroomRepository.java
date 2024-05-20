package com.dife.api.repository;

import com.dife.api.model.Chatroom;
import com.dife.api.model.ChatroomType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

	Boolean existsByName(@Param("name") String name);

	Optional<Chatroom> findByIdAndChatroomType(
			@Param("room_id") Long roomId, @Param("type") ChatroomType type);

	List<Chatroom> findByChatroomType(@Param("type") ChatroomType type);
}
