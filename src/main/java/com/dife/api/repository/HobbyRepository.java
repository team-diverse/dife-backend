package com.dife.api.repository;

import com.dife.api.model.Hobby;
import com.dife.api.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HobbyRepository extends JpaRepository<Hobby, Long> {
    Optional<Hobby> findByMemberAndName(@Param("member") Member member, @Param("hobbyName") String hobbyName);
}
