package com.dife.api.repository;

import com.dife.api.model.Bookmark;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	List<Bookmark> findScrapsByChatroomId(Long room_id);

	Optional<Bookmark> findByChatroomIdAndId(
			@Param("room_id") Long room_id, @Param("chatScrap_id") Long chatScrap_id);
}
