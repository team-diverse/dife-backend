package com.dife.api.repository;

import com.dife.api.model.LikePost;
import com.dife.api.model.Member;
import com.dife.api.model.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {

	List<LikePost> findLikePostsByMember(Member member);

	Optional<LikePost> findByPostAndMember(Post post, Member member);

	boolean existsByPostAndMember(Post post, Member member);
}
