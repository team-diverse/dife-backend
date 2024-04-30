package com.dife.api.repository;

import com.dife.api.model.WebSocketSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebSocketSessionRepository extends JpaRepository<WebSocketSessionEntity, Long> {}
