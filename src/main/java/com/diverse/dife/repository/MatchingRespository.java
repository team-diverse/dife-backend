package com.diverse.dife.repository;

import com.diverse.dife.entity.matching.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRespository extends JpaRepository<Matching, Long> {
}
