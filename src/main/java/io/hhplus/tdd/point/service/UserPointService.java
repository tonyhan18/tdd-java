package io.hhplus.tdd.point.service;

import java.util.Comparator;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.entity.UserPoint;

import io.hhplus.tdd.point.entity.TransactionType;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 사용자 포인트 관련 비즈니스 로직을 담당하는 서비스
 */
@Service
public class UserPointService {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public UserPointService(UserPointTable userPointTable, PointHistoryTable pointHistoryTable) {
        this.userPointTable = userPointTable;
        this.pointHistoryTable = pointHistoryTable;
    }

    /**
     * 사용자 포인트 조회
     * @param userId 사용자 ID
     * @return 사용자 포인트 정보
     */
    public UserPoint getUserPoint(long userId) {
        return userPointTable.selectById(userId);
    }

    /**
     * 포인트 거래 내역 조회
     * @param userId 사용자 ID
     * @return 포인트 거래 내역
     */
    public List<PointHistory> getTransactionHistory(long userId) {
        return pointHistoryTable.selectAllByUserId(userId).stream()
            .sorted(Comparator.comparing(PointHistory::updateMillis).reversed())
            .toList();
    }
    
    /**
     * 포인트 충전
     * @param userId 사용자 ID
     * @param chargeAmount 충전할 금액 (양수여야 함)
     * @return 충전된 사용자 포인트 정보
     * @throws IllegalArgumentException 최대 포인트를 초과하는 경우
     */
    public UserPoint chargePoint(long userId, long chargeAmount) {
        // 현재 포인트 조회
        UserPoint currentUserPoint = userPointTable.selectById(userId);
        
        // 충전 로직 적용
        long newBalance = currentUserPoint.point() + chargeAmount;
        
        // 포인트 업데이트 (Entity에서 자동으로 검증됨)
        UserPoint updatedUserPoint = userPointTable.insertOrUpdate(userId, newBalance);
        
        // 거래 내역 기록
        pointHistoryTable.insert(userId, chargeAmount, TransactionType.CHARGE, updatedUserPoint.updateMillis());
        
        return updatedUserPoint;
    }


    /**
     * 포인트 사용
     * @param userId 사용자 ID
     * @param useAmount 사용할 금액 (양수여야 함)
     * @return 사용된 사용자 포인트 정보
     * @throws IllegalArgumentException 잔액이 부족한 경우
     */
    public UserPoint usePoint(long userId, long useAmount) {
        // 현재 포인트 조회
        UserPoint currentUserPoint = userPointTable.selectById(userId);
        
        // 잔액 확인 (Entity에서 검증)
        currentUserPoint.validateSufficientBalance(useAmount);
        
        // 사용 로직 적용
        long newBalance = currentUserPoint.point() - useAmount;
        
        // 포인트 업데이트 (Entity에서 자동으로 검증됨)
        UserPoint updatedUserPoint = userPointTable.insertOrUpdate(userId, newBalance);
        
        // 거래 내역 기록
        pointHistoryTable.insert(userId, useAmount, TransactionType.USE, updatedUserPoint.updateMillis());
        
        return updatedUserPoint;
    }
}