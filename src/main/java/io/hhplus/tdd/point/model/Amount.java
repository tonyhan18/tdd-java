package io.hhplus.tdd.point.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 포인트 금액을 나타내는 값 객체
 * 금액과 관련된 비즈니스 로직을 캡슐화합니다.
 */
@Schema(description = "포인트 금액")
public class Amount {
    
    @Schema(description = "금액", example = "10000")
    private final long value;
    
    // 최소 금액 상수
    public static final long MIN_AMOUNT = 0L;
    public static final long MAX_AMOUNT = 1_000_000_000L; // 10억
    
    /**
     * Amount 생성자
     * @param value 금액 (0 이상이어야 함)
     * @throws IllegalArgumentException 금액이 음수이거나 최대값을 초과하는 경우
     */
    public Amount(long value) {
        validateAmount(value);
        this.value = value;
    }
    
    /**
     * 금액 유효성 검증
     * @param amount 검증할 금액
     * @throws IllegalArgumentException 유효하지 않은 금액인 경우
     */
    private void validateAmount(long amount) {
        if (amount < MIN_AMOUNT) {
            throw new IllegalArgumentException("금액은 0 이상이어야 합니다. 입력된 금액: " + amount);
        }
        if (amount > MAX_AMOUNT) {
            throw new IllegalArgumentException("금액은 " + MAX_AMOUNT + "을 초과할 수 없습니다. 입력된 금액: " + amount);
        }
    }
    
    /**
     * 현재 금액 반환
     * @return 금액 값
     */
    public long getValue() {
        return value;
    }
    
    /**
     * 0원인지 확인
     * @return 0원이면 true, 아니면 false
     */
    public boolean isZero() {
        return value == 0L;
    }
    
    /**
     * 양수인지 확인
     * @return 양수이면 true, 아니면 false
     */
    public boolean isPositive() {
        return value > 0L;
    }
    
    /**
     * 다른 금액과 더하기
     * @param other 더할 금액
     * @return 새로운 Amount 객체
     */
    public Amount add(Amount other) {
        return new Amount(this.value + other.value);
    }
    
    /**
     * 다른 금액 빼기
     * @param other 뺄 금액
     * @return 새로운 Amount 객체
     * @throws IllegalArgumentException 결과가 음수가 되는 경우
     */
    public Amount subtract(Amount other) {
        long result = this.value - other.value;
        if (result < 0) {
            throw new IllegalArgumentException("잔액이 부족합니다. 현재 잔액: " + this.value + ", 사용 금액: " + other.value);
        }
        return new Amount(result);
    }
    
    /**
     * 다른 금액보다 크거나 같은지 확인
     * @param other 비교할 금액
     * @return 크거나 같으면 true, 아니면 false
     */
    public boolean isGreaterThanOrEqual(Amount other) {
        return this.value >= other.value;
    }
    
    /**
     * 다른 금액보다 큰지 확인
     * @param other 비교할 금액
     * @return 크면 true, 아니면 false
     */
    public boolean isGreaterThan(Amount other) {
        return this.value > other.value;
    }
    
    /**
     * 금액을 문자열로 포맷팅 (천 단위 콤마 포함)
     * @return 포맷팅된 금액 문자열
     */
    public String format() {
        return String.format("%,d", value);
    }
    
    /**
     * 0원 Amount 생성
     * @return 0원 Amount 객체
     */
    public static Amount zero() {
        return new Amount(0L);
    }
    
    /**
     * long 값으로부터 Amount 생성
     * @param value 금액
     * @return Amount 객체
     */
    public static Amount of(long value) {
        return new Amount(value);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Amount amount = (Amount) obj;
        return value == amount.value;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }
    
    @Override
    public String toString() {
        return "Amount{value=" + format() + "}";
    }
} 