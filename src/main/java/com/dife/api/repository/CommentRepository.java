package com.dife.api.repository;

import com.dife.api.model.Comment;
import com.dife.api.model.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findCommentsByPost(Post post);
}
