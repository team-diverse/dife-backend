package com.dife.api.repository;

import com.dife.api.model.BoardCategory;
import com.dife.api.model.Member;
import com.dife.api.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostsByBoardType(BoardCategory boardType);
    Optional<Post> findByMemberAndId(@Param("member") Member member, @Param("postId") Long postId);
}
