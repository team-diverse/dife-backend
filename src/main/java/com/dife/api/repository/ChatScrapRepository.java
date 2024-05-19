package com.dife.api.repository;

import com.dife.api.model.ChatScrap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatScrapRepository extends JpaRepository<ChatScrap, Long> {

	List<ChatScrap> findScrapsByChatroomId(Long room_id);
}
