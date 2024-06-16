package com.dife.api.repository;

import com.dife.api.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {

	boolean existsByCommentAndMember(Comment comment, Member member);
}
