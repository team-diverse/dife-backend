package com.dife.api.repository;

import com.dife.api.model.ChatroomSetting;
import com.dife.api.model.Language;
import com.dife.api.model.Member;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

	Set<Language> findLanguagesByMember(Member member);

	Set<Language> findLanguagesByChatroomSetting(ChatroomSetting chatroomSetting);
}
