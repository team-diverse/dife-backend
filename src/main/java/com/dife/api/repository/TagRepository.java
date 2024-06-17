package com.dife.api.repository;

import com.dife.api.model.*;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

	Set<Tag> findTagsByChatroomSetting(ChatroomSetting chatroomSetting);
}
