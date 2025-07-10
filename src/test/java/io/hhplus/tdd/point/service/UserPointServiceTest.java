package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.entity.UserPoint;

import io.hhplus.tdd.point.entity.TransactionType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserPointService 핵심 기능 테스트")
class UserPointServiceTest {

    @Mock
    UserPointTable userPointTable;

    @Mock
    PointHistoryTable pointHistoryTable;

    @Mock
    UserPointLockProvider lockProvider;

    @InjectMocks
    UserPointService userPointService;


    @Test
    @DisplayName("사용자 포인트 조회")
    void getUserPoint_ShouldReturnUserPoint() {
        // given
        long userId = 1L;
        UserPoint expectedUserPoint = new UserPoint(userId, 1000L, System.currentTimeMillis());
        when(userPointTable.selectById(userId)).thenReturn(expectedUserPoint);

        // when
        UserPoint result = userPointService.getUserPoint(userId);

        // then
        assertThat(result).isEqualTo(expectedUserPoint);
    }

    @Test
    @DisplayName("포인트 충전")
    void chargePoint_ShouldChargePoint() {
        // given
        long userId = 1L;
        long chargeAmount = 1000L;
        long currentBalance = 500L;
        long newBalance = currentBalance + chargeAmount;
        
        UserPoint currentUserPoint = new UserPoint(userId, currentBalance, System.currentTimeMillis());
        UserPoint updatedUserPoint = new UserPoint(userId, newBalance, System.currentTimeMillis());
        
        // 0708 lock 취득 추가
        when(lockProvider.getLock(userId)).thenReturn(new ReentrantLock());
        when(userPointTable.selectById(userId)).thenReturn(currentUserPoint);
        when(userPointTable.insertOrUpdate(userId, newBalance)).thenReturn(updatedUserPoint);
        when(pointHistoryTable.insert(eq(userId), eq(chargeAmount), eq(TransactionType.CHARGE), anyLong()))
            .thenReturn(new PointHistory(1L, userId, chargeAmount, TransactionType.CHARGE, updatedUserPoint.updateMillis()));

        // when
        UserPoint result = userPointService.chargePoint(userId, chargeAmount);

        // then
        assertThat(result).isEqualTo(updatedUserPoint);
        verify(pointHistoryTable).insert(userId, chargeAmount, TransactionType.CHARGE, updatedUserPoint.updateMillis());
    }

    @Test
    @DisplayName("포인트 사용 - 성공")
    void usePoint_WithSufficientBalance_ShouldUsePoint() {
        // given
        long userId = 1L;
        long useAmount = 500L;
        long currentBalance = 1000L;
        long newBalance = currentBalance - useAmount;
        
        UserPoint currentUserPoint = new UserPoint(userId, currentBalance, System.currentTimeMillis());
        UserPoint updatedUserPoint = new UserPoint(userId, newBalance, System.currentTimeMillis());
        
        // 0708 lock 취득 추가
        when(lockProvider.getLock(userId)).thenReturn(new ReentrantLock());
        when(userPointTable.selectById(userId)).thenReturn(currentUserPoint);
        when(userPointTable.insertOrUpdate(userId, newBalance)).thenReturn(updatedUserPoint);
        when(pointHistoryTable.insert(eq(userId), eq(useAmount), eq(TransactionType.USE), anyLong()))
            .thenReturn(new PointHistory(1L, userId, useAmount, TransactionType.USE, updatedUserPoint.updateMillis()));

        // when
        UserPoint result = userPointService.usePoint(userId, useAmount);

        // then
        assertThat(result).isEqualTo(updatedUserPoint);
        verify(pointHistoryTable).insert(userId, useAmount, TransactionType.USE, updatedUserPoint.updateMillis());
    }

    @Test
    @DisplayName("포인트 사용 - 잔액 부족 예외")
    void usePoint_WithInsufficientBalance_ShouldThrowException() {
        // given
        long userId = 1L;
        long useAmount = 1000L;
        long currentBalance = 500L;
        
        UserPoint currentUserPoint = new UserPoint(userId, currentBalance, System.currentTimeMillis());
        // 0708 lock 취득 추가
        when(lockProvider.getLock(userId)).thenReturn(new ReentrantLock());
        when(userPointTable.selectById(userId)).thenReturn(currentUserPoint);

        // when & then
        assertThatThrownBy(() -> userPointService.usePoint(userId, useAmount))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("잔액이 부족합니다");
    }

    @Test
    @DisplayName("포인트 거래 내역 조회")
    void getTransactionHistory_ShouldReturnPointHistoryList() {
        // given
        long userId = 1L;
        List<PointHistory> expectedHistory = List.of(
            new PointHistory(1L, userId, 1000L, TransactionType.CHARGE, System.currentTimeMillis()),
            new PointHistory(2L, userId, 500L, TransactionType.USE, System.currentTimeMillis())
        );
        when(pointHistoryTable.selectAllByUserId(userId)).thenReturn(expectedHistory);

        // when
        List<PointHistory> result = userPointService.getTransactionHistory(userId);

        // then
        assertThat(result).isEqualTo(expectedHistory);
    }
} 