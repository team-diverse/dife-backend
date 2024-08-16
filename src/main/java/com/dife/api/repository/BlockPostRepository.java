package com.dife.api.repository;

import com.dife.api.model.Member;
import com.dife.api.model.Post;
import com.dife.api.model.PostBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockPostRepository extends JpaRepository<PostBlock, Long> {

	boolean existsByPostAndMember(Post post, Member member);
}
