package io.hhplus.tdd.point.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PointHistory 엔티티 테스트")
class PointHistoryTest {

    @Test
    @DisplayName("정상적으로 PointHistory를 생성할 수 있다")
    void createPointHistory() {
        long id = 1L;
        long userId = 1L;
        long amount = 1000L;
        TransactionType type = TransactionType.CHARGE;
        long updateMillis = System.currentTimeMillis();

        PointHistory history = new PointHistory(id, userId, amount, type, updateMillis);

        assertThat(history.id()).isEqualTo(id);
        assertThat(history.userId()).isEqualTo(userId);
        assertThat(history.amount()).isEqualTo(amount);
        assertThat(history.type()).isEqualTo(type);
        assertThat(history.updateMillis()).isEqualTo(updateMillis);
    }

    @Test
    @DisplayName("equals() 메서드가 정상적으로 동작한다")
    void equals_ReturnsCorrectValue() {
        long updateMillis = System.currentTimeMillis();
        PointHistory history1 = new PointHistory(1L, 1L, 10000L, TransactionType.CHARGE, updateMillis);
        PointHistory history2 = new PointHistory(1L, 1L, 10000L, TransactionType.CHARGE, updateMillis);
        PointHistory history3 = new PointHistory(2L, 1L, 10000L, TransactionType.CHARGE, updateMillis);

        assertThat(history1).isEqualTo(history2);
        assertThat(history1).isNotEqualTo(history3);
        assertThat(history1).isEqualTo(history1);
    }
}
