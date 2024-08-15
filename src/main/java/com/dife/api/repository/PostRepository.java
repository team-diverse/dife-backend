package com.dife.api.repository;

import com.dife.api.model.BoardCategory;
import com.dife.api.model.Member;
import com.dife.api.model.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findPostsByBoardType(BoardCategory boardType, Sort sort);

	Optional<Post> findByWriterAndId(Member writer, Long id);

	List<Post> findPostsByWriter(Member writer, Sort sort);

	List<Post> findAllByWriter(Member writer);

	@Query(
			"SELECT p FROM Post p "
					+ "WHERE (p.title LIKE %:keyword% "
					+ "OR p.content LIKE %:keyword% "
					+ "OR p.writer.name LIKE %:keyword%)")
	List<Post> findAllByKeywordSearch(@Param("keyword") String keyword);
}
