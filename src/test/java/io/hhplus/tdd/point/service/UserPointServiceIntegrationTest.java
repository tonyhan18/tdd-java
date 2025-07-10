package io.hhplus.tdd.point.service;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.entity.TransactionType;
import io.hhplus.tdd.point.entity.UserPoint;

@DisplayName("UserPointService 통합 테스트")
class UserPointServiceIntegrationTest {

    @Test
    @DisplayName("포인트 충전/사용/거래내역/잔액조회가 정상 동작한다")
    void basicUsage() {
        UserPointTable userPointTable = new UserPointTable();
        PointHistoryTable pointHistoryTable = new PointHistoryTable();
        UserPointLockProvider lockProvider = new UserPointLockProvider();
        UserPointService service = new UserPointService(userPointTable, pointHistoryTable, lockProvider);

        // 충전
        UserPoint charged = service.chargePoint(1L, 10000L);
        assertThat(charged.point()).isEqualTo(10000L);

        // 사용
        UserPoint used = service.usePoint(1L, 3000L);
        assertThat(used.point()).isEqualTo(7000L);

        // 거래내역
        List<PointHistory> histories = service.getTransactionHistory(1L);
        assertThat(histories).hasSize(2);
        assertThat(histories.get(0).type()).isEqualTo(TransactionType.USE);
        assertThat(histories.get(1).type()).isEqualTo(TransactionType.CHARGE);

        // 잔액조회
        UserPoint userPoint = service.getUserPoint(1L);
        assertThat(userPoint.point()).isEqualTo(7000L);
    }

    @Test
    @DisplayName("동일 사용자의 동시 충전 요청이 올바르게 처리된다 (동시성 테스트)")
    void concurrentCharge() throws InterruptedException {
        UserPointTable userPointTable = new UserPointTable();
        PointHistoryTable pointHistoryTable = new PointHistoryTable();
        UserPointLockProvider lockProvider = new UserPointLockProvider();
        UserPointService service = new UserPointService(userPointTable, pointHistoryTable, lockProvider);

        int threadCount = 10;
        long chargeAmount = 1000L;
        CountDownLatch latch = new CountDownLatch(threadCount);

        var executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    service.chargePoint(1L, chargeAmount);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        UserPoint userPoint = service.getUserPoint(1L);
        assertThat(userPoint.point()).isEqualTo(threadCount * chargeAmount);

        List<PointHistory> histories = service.getTransactionHistory(1L);
        assertThat(histories).hasSize(threadCount);
    }

    @Test
    @DisplayName("동일 사용자의 동시 사용 요청이 올바르게 처리된다 (잔액 부족 예외 포함)")
    void concurrentUse_withInsufficientBalance() throws InterruptedException {
        UserPointTable userPointTable = new UserPointTable();
        PointHistoryTable pointHistoryTable = new PointHistoryTable();
        UserPointLockProvider lockProvider = new UserPointLockProvider();
        UserPointService service = new UserPointService(userPointTable, pointHistoryTable, lockProvider);

        // 5000원 충전
        service.chargePoint(1L, 5000L);

        int threadCount = 10;
        long useAmount = 1000L;
        CountDownLatch latch = new CountDownLatch(threadCount);

        var executor = Executors.newFixedThreadPool(threadCount);

        int[] successCount = {0};
        int[] failCount = {0};

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    service.usePoint(1L, useAmount);
                    synchronized (successCount) { successCount[0]++; }
                } catch (IllegalArgumentException e) {
                    synchronized (failCount) { failCount[0]++; }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // 최대 5번만 성공, 나머지는 잔액 부족 예외
        assertThat(successCount[0]).isEqualTo(5);
        assertThat(failCount[0]).isEqualTo(5);

        UserPoint userPoint = service.getUserPoint(1L);
        assertThat(userPoint.point()).isEqualTo(0L);

        List<PointHistory> histories = service.getTransactionHistory(1L);
        assertThat(histories.stream().filter(h -> h.type() == TransactionType.USE)).hasSize(5);
    }
}