package com.dife.api.repository;

import com.dife.api.model.ChatroomSetting;
import com.dife.api.model.GroupPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupPurposesRepository extends JpaRepository<GroupPurpose, Long> {

	@Query(
			"SELECT COUNT(p) > 0 FROM GroupPurpose p WHERE p.name = :name AND p.chatroom_setting = :chatroomSetting")
	Boolean existsGroupPurposeByNameAndChatroomSetting(
			@Param("name") String name, @Param("chatroomSetting") ChatroomSetting chatroomSetting);
}
