package com.dife.api.repository;

import com.dife.api.model.BoardCategory;
import com.dife.api.model.Member;
import com.dife.api.model.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findPostsByBoardType(BoardCategory boardType, Sort sort);

	Optional<Post> findByMemberAndId(Member member, Long id);

	List<Post> findPostsByMember(Member member, Sort sort);
}
