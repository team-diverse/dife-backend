package com.dife.api.repository;

import com.dife.api.model.Bookmark;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	List<Bookmark> findBookmarksByMemberEmail(String email);

	@Query("SELECT b FROM Bookmark b WHERE (b.member.email = :email AND b.id = :id)")
	Optional<Bookmark> findByMemberEmailAndId(@Param("id") Long id, @Param("email") String email);
}
