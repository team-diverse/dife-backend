package com.dife.api.repository;

import com.dife.api.model.ChatroomSetting;
import com.dife.api.model.Language;
import com.dife.api.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

	@Query(
			"SELECT COUNT(l) > 0 FROM Language l WHERE l.name = :name AND l.chatroom_setting = :chatroomSetting")
	Boolean existsLanguageByNameAndChatroomSetting(
			@Param("name") String name, @Param("chatroomSetting") ChatroomSetting chatroomSetting);

	@Query("SELECT COUNT(l) > 0 FROM Language l WHERE l.name = :name AND l.member = :member")
	Boolean existsLanguageByNameAndMember(@Param("name") String name, @Param("member") Member member);
}
