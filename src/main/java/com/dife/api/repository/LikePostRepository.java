package com.dife.api.repository;

import com.dife.api.model.Member;
import com.dife.api.model.Post;
import com.dife.api.model.PostLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikePostRepository extends JpaRepository<PostLike, Long> {

	List<PostLike> findPostLikesByMember(Member member);

	Optional<PostLike> findByPostAndMember(Post post, Member member);

	boolean existsByPostAndMember(Post post, Member member);
}
