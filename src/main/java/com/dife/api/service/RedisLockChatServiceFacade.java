package com.dife.api.service;

import com.dife.api.model.*;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisLockChatServiceFacade {
	@Autowired private RedissonClient redissonClient;
	@Autowired private ChatroomService chatroomService;

	@Transactional
	public void increase(Long chatroomId, String sessionId) {
		RLock lock = redissonClient.getLock("lock:" + chatroomId.toString());

		try {
			boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
			if (!available) {
				return;
			}
			chatroomService.increase(chatroomId, sessionId);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Interrupted during lock acquisition", e);
		} finally {
			if (lock.isLocked() && lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	@Transactional
	public void decrease(Long chatroomId, String sessionId) {
		RLock lock = redissonClient.getLock("lock:" + chatroomId.toString());

		try {
			boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
			if (!available) {
				return;
			}
			chatroomService.decrease(chatroomId, sessionId);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Interrupted during lock acquisition", e);
		} finally {
			if (lock.isLocked() && lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
}
