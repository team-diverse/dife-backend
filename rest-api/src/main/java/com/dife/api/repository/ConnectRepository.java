package com.dife.api.repository;

import com.dife.api.model.Connect;
import com.dife.api.model.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConnectRepository extends JpaRepository<Connect, Long> {
    @Query("SELECT c FROM Connect c WHERE (c.toMember = :member1 AND c.fromMember = :member2) OR (c.fromMember = :member1 AND c.toMember = :member2) ")
    Optional<Connect> findByMemberPair(@Param("member1") Member member1, @Param("member2") Member member2);

    Optional<Connect> findByFromMemberAndToMember(Member fromMember, Member toMember);
}
