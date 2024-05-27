package com.dife.api.repository;

import com.dife.api.model.Hobby;
import com.dife.api.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HobbyRepository extends JpaRepository<Hobby, Long> {
	@Query("SELECT COUNT(h) > 0 FROM Hobby h WHERE h.name = :name AND h.member = :member")
	Boolean existsHobbyByNameAndMember(@Param("name") String name, @Param("member") Member member);
}
