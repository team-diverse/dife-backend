package com.dife.api.repository;

import com.dife.api.model.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	@Query(
			"SELECT m FROM Member m "
					+ "WHERE m.isPublic = true "
					+ "AND (m.name LIKE %:keyword% "
					+ "OR m.username LIKE %:keyword% "
					+ "OR m.major LIKE %:keyword% "
					+ "OR m.studentId LIKE %:keyword% "
					+ "OR m.bio LIKE %:keyword%)")
	List<Member> findAllByKeywordSearch(@Param("keyword") String keyword);
}
