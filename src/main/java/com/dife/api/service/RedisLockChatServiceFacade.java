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

	private static final long WAITTIME = 10L;
	private static final long LEASETIME = 1L;

	@Transactional
	public void increase(Long chatroomId) {
		executeWithLock(chatroomId, () -> chatroomService.increase(chatroomId));
	}

	@Transactional
	public void decrease(Long chatroomId) {
		executeWithLock(chatroomId, () -> chatroomService.decrease(chatroomId));
	}

	private void executeWithLock(Long chatroomId, Runnable action) {
		RLock lock = redissonClient.getLock("lock:" + chatroomId.toString());
		try {
			boolean available = lock.tryLock(WAITTIME, LEASETIME, TimeUnit.SECONDS);
			if (!available) {
				return;
			}
			action.run();
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
