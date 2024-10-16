package com.dife.api.repository;

import com.dife.api.model.Bookmark;
import com.dife.api.model.Member;
import com.dife.api.model.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	@Query(
			"SELECT b FROM Bookmark b "
					+ "JOIN b.member m "
					+ "JOIN Chatroom cr ON cr.id = :chatroomId "
					+ "JOIN cr.chats c "
					+ "WHERE m = :member AND b.message = c.message")
	List<Bookmark> findBookmarksByMemberAndChatroom(
			@Param("member") Member member, @Param("chatroomId") Long chatroomId);

	List<Bookmark> findAllByMember(Member member);

	boolean existsBookmarkByPostAndMember(Post post, Member member);

	Optional<Bookmark> findBookmarkByPostAndMember(Post post, Member member);

	boolean existsBookmarkByMessage(@Param("message") String message);
}
