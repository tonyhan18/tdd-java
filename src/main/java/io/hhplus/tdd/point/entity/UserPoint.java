package io.hhplus.tdd.point.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 포인트 정보")
public record UserPoint(
        @Schema(description = "사용자 ID", example = "1") long id,
        @Schema(description = "현재 포인트 잔액", example = "15000") long point,
        @Schema(description = "마지막 업데이트 시간 (밀리초)", example = "1703123456789") long updateMillis
) {
    private static final long MIN_POINT = 0;
    private static final long MAX_POINT = 10_000_000;

    public UserPoint {
        if (point < MIN_POINT) {
            throw new IllegalArgumentException("잔고가 부족합니다.");
        }

        if (point > MAX_POINT) {
            throw new IllegalArgumentException("최대 잔고는 10,000,000 포인트 입니다.");
        }
    }

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public long addPoint(long amount) {
        return point + amount;
    }
    
    public boolean hasSufficientBalance(long amount) {
        return point >= amount;
    }
    
    public void validateSufficientBalance(long amount) {
        if (!hasSufficientBalance(amount)) {
            throw new IllegalArgumentException(
                String.format("잔액이 부족합니다. 현재 잔액: %d, 사용 금액: %d", point, amount)
            );
        }
    }
    
    public static long getMaxPoint() {
        return MAX_POINT;
    }
    
    public static long getMinPoint() {
        return MIN_POINT;
    }
} 