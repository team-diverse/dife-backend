package com.dife.api.repository;

import com.dife.api.model.*;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

	Optional<CommentLike> findByCommentAndMember(Comment comment, Member member);

	boolean existsByCommentAndMember(Comment comment, Member member);
}
