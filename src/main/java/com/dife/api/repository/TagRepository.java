package com.dife.api.repository;

import com.dife.api.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

	@Query(
			"SELECT COUNT(t) > 0 FROM Tag t WHERE t.name = :name AND t.chatroom_setting = :chatroomSetting")
	Boolean existsTagByNameAndChatroomSetting(
			@Param("name") String name, @Param("chatroomSetting") ChatroomSetting chatroomSetting);
}
