package com.dife.api.repository;

import com.dife.api.model.Hobby;
import com.dife.api.model.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface HobbyRepository extends JpaRepository<Hobby, Long> {
	Optional<Hobby> findByMemberAndName(
			@Param("member") Member member, @Param("hobbyName") String hobbyName);
}
