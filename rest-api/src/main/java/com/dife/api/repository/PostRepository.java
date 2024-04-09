package com.dife.api.repository;

import com.dife.api.model.BOARD_category;
import com.dife.api.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostsByBoardType(BOARD_category boardType);
}
