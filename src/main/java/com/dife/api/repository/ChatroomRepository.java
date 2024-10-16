package com.dife.api.repository;

import com.dife.api.model.Chatroom;
import com.dife.api.model.ChatroomType;
import com.dife.api.model.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

	Boolean existsByName(String name);

	@Query(
			"SELECT COUNT(c) > 0 FROM Chatroom c JOIN c.members m WHERE m = :member1 AND :member2 MEMBER OF c.members AND c.chatroomType = :chatroomType")
	Boolean existsSingleChatroomByMembers(
			@Param("member1") Member member1,
			@Param("member2") Member member2,
			@Param("chatroomType") ChatroomType chatroomType);

	List<Chatroom> findAllByChatroomType(ChatroomType chatroomType);

	List<Chatroom> findAllByChatroomTypeAndManager(ChatroomType chatroomType, Member manager);

	List<Chatroom> findAllByChatroomTypeAndMembersContains(ChatroomType chatroomType, Member member);

	@Query(
			"""
				SELECT c FROM Chatroom c
						WHERE c.chatroomSetting.isPublic = true
						AND (c.name LIKE %:keyword%
						OR c.chatroomSetting.description LIKE %:keyword%)
				""")
	List<Chatroom> findAllByKeywordSearch(@Param("keyword") String keyword);
}
