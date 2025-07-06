package io.hhplus.tdd.point.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("UserPoint 엔티티 테스트")
class UserPointTest {

    @Test
    @DisplayName("정상적인 값으로 UserPoint를 생성할 수 있다")
    void createUserPointWithValidValues() {
        long id = 1L;
        long point = 10000L;
        long updateMillis = System.currentTimeMillis();

        UserPoint userPoint = new UserPoint(id, point, updateMillis);

        assertThat(userPoint.id()).isEqualTo(id);
        assertThat(userPoint.point()).isEqualTo(point);
        assertThat(userPoint.updateMillis()).isEqualTo(updateMillis);
    }

    @Test
    @DisplayName("음수 포인트로 UserPoint를 생성하면 예외가 발생한다")
    void createUserPointWithNegativePoint_ThrowsException() {
        long id = 1L;
        long point = -1000L;
        long updateMillis = System.currentTimeMillis();

        assertThatThrownBy(() -> new UserPoint(id, point, updateMillis))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잔고가 부족합니다.");
    }

    @Test
    @DisplayName("최대 포인트를 초과하여 UserPoint를 생성하면 예외가 발생한다")
    void createUserPointWithExceedMaxPoint_ThrowsException() {
        long id = 1L;
        long point = 10_000_001L;
        long updateMillis = System.currentTimeMillis();

        assertThatThrownBy(() -> new UserPoint(id, point, updateMillis))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("최대 잔고는 10,000,000 포인트 입니다.");
    }

    @Test
    @DisplayName("empty() 정적 메서드가 올바른 UserPoint를 반환한다")
    void empty_ReturnsCorrectUserPoint() {
        long id = 1L;

        UserPoint userPoint = UserPoint.empty(id);

        assertThat(userPoint.id()).isEqualTo(id);
        assertThat(userPoint.point()).isEqualTo(0L);
        assertThat(userPoint.updateMillis()).isGreaterThan(0L);
    }

    @Test
    @DisplayName("addPoint() 메서드가 올바른 결과를 반환한다")
    void addPoint_ReturnsCorrectResult() {
        UserPoint userPoint = new UserPoint(1L, 5000L, System.currentTimeMillis());
        long amount = 3000L;

        long result = userPoint.addPoint(amount);

        assertThat(result).isEqualTo(8000L);
    }

    @Test
    @DisplayName("잔액이 충분하면 validateSufficientBalance가 예외를 발생시키지 않는다")
    void validateSufficientBalance_WithEnoughBalance_DoesNotThrow() {
        UserPoint userPoint = new UserPoint(1L, 5000L, System.currentTimeMillis());
        assertThatCode(() -> userPoint.validateSufficientBalance(3000L)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("잔액이 부족하면 validateSufficientBalance가 예외를 발생시킨다")
    void validateSufficientBalance_WithInsufficientBalance_ThrowsException() {
        UserPoint userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());
        assertThatThrownBy(() -> userPoint.validateSufficientBalance(2000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잔액이 부족합니다");
    }

    @Test
    @DisplayName("equals() 메서드가 정상적으로 동작한다")
    void equals_ReturnsCorrectValue() {
        long updateMillis = System.currentTimeMillis();
        UserPoint userPoint1 = new UserPoint(1L, 10000L, updateMillis);
        UserPoint userPoint2 = new UserPoint(1L, 10000L, updateMillis);
        UserPoint userPoint3 = new UserPoint(2L, 10000L, updateMillis);

        assertThat(userPoint1).isEqualTo(userPoint2);
        assertThat(userPoint1).isNotEqualTo(userPoint3);
        assertThat(userPoint1).isEqualTo(userPoint1);
    }
}
