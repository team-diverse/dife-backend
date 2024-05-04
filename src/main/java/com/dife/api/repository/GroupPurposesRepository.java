package com.dife.api.repository;

import com.dife.api.model.GroupPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupPurposesRepository extends JpaRepository<GroupPurpose, Long> {}
