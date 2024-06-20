package com.dife.api.repository;

import com.dife.api.model.Hobby;
import com.dife.api.model.Member;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HobbyRepository extends JpaRepository<Hobby, Long> {
	Set<Hobby> findHobbiesByMember(Member member);
}
