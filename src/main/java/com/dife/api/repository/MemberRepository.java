package com.dife.api.repository;

import com.dife.api.model.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);
}
