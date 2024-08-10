package com.dife.api.repository;

import com.dife.api.model.*;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeChatroomRepository extends JpaRepository<ChatroomLike, Long> {

	Optional<ChatroomLike> findByChatroomAndMember(Chatroom chatroom, Member member);

	boolean existsByChatroomAndMember(Chatroom chatroom, Member member);
}
