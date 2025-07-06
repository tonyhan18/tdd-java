package io.hhplus.tdd.database;

import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.entity.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PointHistoryTable 단위 테스트")
class PointHistoryTableTest {

    @Test
    @DisplayName("insert로 거래 내역을 추가하면 selectAllByUserId로 조회할 수 있다")
    void insertAndSelectAllByUserId() {
        PointHistoryTable table = new PointHistoryTable();

        PointHistory history = table.insert(1L, 1000L, TransactionType.CHARGE, System.currentTimeMillis());

        List<PointHistory> result = table.selectAllByUserId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(history);
    }

    @Test
    @DisplayName("여러 거래 내역을 추가하면 모두 조회된다")
    void insertMultipleHistories() {
        PointHistoryTable table = new PointHistoryTable();

        PointHistory h1 = table.insert(1L, 1000L, TransactionType.CHARGE, System.currentTimeMillis());
        PointHistory h2 = table.insert(1L, 2000L, TransactionType.USE, System.currentTimeMillis());
        PointHistory h3 = table.insert(2L, 3000L, TransactionType.CHARGE, System.currentTimeMillis());

        List<PointHistory> user1Histories = table.selectAllByUserId(1L);
        List<PointHistory> user2Histories = table.selectAllByUserId(2L);

        assertThat(user1Histories).containsExactly(h1, h2);
        assertThat(user2Histories).containsExactly(h3);
    }

    @Test
    @DisplayName("거래 내역이 없는 사용자는 빈 리스트를 반환한다")
    void selectAllByUserId_NoHistory() {
        PointHistoryTable table = new PointHistoryTable();

        List<PointHistory> result = table.selectAllByUserId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("insert 시 거래 내역 ID가 자동 증가한다")
    void insert_AutoIncrementId() {
        PointHistoryTable table = new PointHistoryTable();

        PointHistory h1 = table.insert(1L, 1000L, TransactionType.CHARGE, System.currentTimeMillis());
        PointHistory h2 = table.insert(1L, 2000L, TransactionType.USE, System.currentTimeMillis());

        assertThat(h2.id()).isEqualTo(h1.id() + 1);
    }
}
