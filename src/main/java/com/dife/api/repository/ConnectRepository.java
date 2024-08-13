package com.dife.api.repository;

import com.dife.api.model.Connect;
import com.dife.api.model.ConnectStatus;
import com.dife.api.model.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectRepository extends JpaRepository<Connect, Long> {
	@Query(
			"SELECT c FROM Connect c WHERE (c.toMember = :member OR c.fromMember = :member) AND c.status = :status")
	List<Connect> findAllByMemberAndStatus(
			@Param("member") Member member, @Param("status") ConnectStatus status);

	@Query(
			"SELECT c FROM Connect c WHERE (c.toMember = :member1 AND c.fromMember = :member2) OR (c.fromMember = :member1 AND c.toMember = :member2) ")
	Optional<Connect> findByMemberPair(
			@Param("member1") Member member1, @Param("member2") Member member2);

	Optional<Connect> findByFromMemberAndToMember(Member fromMember, Member toMember);

	List<Connect> findAllByFromMember(Member fromMember);

	List<Connect> findAllByToMember(Member toMember);
}
