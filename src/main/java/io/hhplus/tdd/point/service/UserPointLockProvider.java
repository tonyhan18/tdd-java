package io.hhplus.tdd.point.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Component;


/**
 * 사용자별로 Lock을 제공하는 LockProvider
 */
@Component
public class UserPointLockProvider {

    // 사용자 ID별로 Lock을 관리
    private final ConcurrentHashMap<Long, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    /**
     * 해당 사용자 ID에 대한 Lock을 반환
     */
    public Lock getLock(Long userId) {
        // computeIfAbsent: 없으면 새로 생성
        return lockMap.computeIfAbsent(userId, id -> new ReentrantLock());
    }
}
