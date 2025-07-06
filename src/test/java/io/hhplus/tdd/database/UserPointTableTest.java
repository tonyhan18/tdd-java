package io.hhplus.tdd.database;

import io.hhplus.tdd.point.entity.UserPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("UserPointTable 테스트")
class UserPointTableTest {

    private UserPointTable userPointTable;

    @BeforeEach
    void setUp() {
        userPointTable = new UserPointTable();
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 조회하면 빈 UserPoint를 반환한다")
    void selectById_NonExistentUser_ReturnsEmptyUserPoint() {
        // given
        Long userId = 1L;

        // when
        UserPoint result = userPointTable.selectById(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(0L);
        assertThat(result.updateMillis()).isGreaterThan(0L);
    }

    @Test
    @DisplayName("존재하는 사용자 ID로 조회하면 해당 UserPoint를 반환한다")
    void selectById_ExistentUser_ReturnsCorrectUserPoint() {
        // given
        Long userId = 1L;
        long amount = 10000L;
        UserPoint insertedUserPoint = userPointTable.insertOrUpdate(userId, amount);

        // when
        UserPoint result = userPointTable.selectById(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(amount);
        assertThat(result.updateMillis()).isEqualTo(insertedUserPoint.updateMillis());
    }

    @Test
    @DisplayName("새로운 사용자의 포인트를 삽입할 수 있다")
    void insertOrUpdate_NewUser_ReturnsCorrectUserPoint() {
        // given
        Long userId = 1L;
        long amount = 15000L;

        // when
        UserPoint result = userPointTable.insertOrUpdate(userId, amount);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(amount);
        assertThat(result.updateMillis()).isGreaterThan(0L);
    }

    @Test
    @DisplayName("기존 사용자의 포인트를 업데이트할 수 있다")
    void insertOrUpdate_ExistingUser_UpdatesCorrectly() {
        // given
        Long userId = 1L;
        long initialAmount = 10000L;
        long updatedAmount = 20000L;

        userPointTable.insertOrUpdate(userId, initialAmount);

        // when
        UserPoint result = userPointTable.insertOrUpdate(userId, updatedAmount);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(updatedAmount);
        assertThat(result.point()).isNotEqualTo(initialAmount);
    }

    @Test
    @DisplayName("0원 포인트로 사용자를 생성할 수 있다")
    void insertOrUpdate_ZeroAmount_WorksCorrectly() {
        // given
        Long userId = 1L;
        long amount = 0L;

        // when
        UserPoint result = userPointTable.insertOrUpdate(userId, amount);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(0L);
    }

    @Test
    @DisplayName("음수 포인트로 사용자를 생성할 수 있다")
    void insertOrUpdate_NegativeAmount_WorksCorrectly() {
        // given
        Long userId = 1L;
        long amount = -5000L;

        // when
        UserPoint result = userPointTable.insertOrUpdate(userId, amount);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(-5000L);
    }

    @Test
    @DisplayName("여러 사용자의 포인트를 관리할 수 있다")
    void multipleUsers_AreManagedCorrectly() {
        // given
        Long userId1 = 1L;
        Long userId2 = 2L;
        Long userId3 = 3L;
        long amount1 = 10000L;
        long amount2 = 20000L;
        long amount3 = 30000L;

        // when
        UserPoint user1 = userPointTable.insertOrUpdate(userId1, amount1);
        UserPoint user2 = userPointTable.insertOrUpdate(userId2, amount2);
        UserPoint user3 = userPointTable.insertOrUpdate(userId3, amount3);

        // then
        assertThat(userPointTable.selectById(userId1)).isEqualTo(user1);
        assertThat(userPointTable.selectById(userId2)).isEqualTo(user2);
        assertThat(userPointTable.selectById(userId3)).isEqualTo(user3);
    }

    @Test
    @DisplayName("사용자 포인트를 여러 번 업데이트할 수 있다")
    void multipleUpdates_WorkCorrectly() {
        // given
        Long userId = 1L;
        long amount1 = 10000L;
        long amount2 = 20000L;
        long amount3 = 15000L;

        // when
        UserPoint firstUpdate = userPointTable.insertOrUpdate(userId, amount1);
        UserPoint secondUpdate = userPointTable.insertOrUpdate(userId, amount2);
        UserPoint thirdUpdate = userPointTable.insertOrUpdate(userId, amount3);

        // then
        assertThat(firstUpdate.point()).isEqualTo(amount1);
        assertThat(secondUpdate.point()).isEqualTo(amount2);
        assertThat(thirdUpdate.point()).isEqualTo(amount3);
        assertThat(userPointTable.selectById(userId).point()).isEqualTo(amount3);
    }

    @Test
    @DisplayName("큰 금액의 포인트를 처리할 수 있다")
    void largeAmount_IsHandledCorrectly() {
        // given
        Long userId = 1L;
        long largeAmount = 999999999L;

        // when
        UserPoint result = userPointTable.insertOrUpdate(userId, largeAmount);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(largeAmount);
        assertThat(userPointTable.selectById(userId).point()).isEqualTo(largeAmount);
    }

    @Test
    @DisplayName("삽입된 UserPoint의 모든 필드가 정확히 저장된다")
    void insertOrUpdate_AllFieldsAreCorrectlyStored() {
        // given
        Long userId = 123L;
        long amount = 45678L;

        // when
        UserPoint result = userPointTable.insertOrUpdate(userId, amount);

        // then
        assertThat(result.id()).isEqualTo(123L);
        assertThat(result.point()).isEqualTo(45678L);
        assertThat(result.updateMillis()).isGreaterThan(0L);
    }

    @Test
    @DisplayName("null 사용자 ID로 조회해도 예외가 발생하지 않는다")
    void selectById_NullUserId_ReturnsEmptyUserPoint() {
        // given
        Long userId = null;

        // when & then
        assertThatCode(() -> {
            UserPoint result = userPointTable.selectById(userId);
            assertThat(result).isNotNull();
            assertThat(result.id()).isNull();
            assertThat(result.point()).isEqualTo(0L);
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("동일한 사용자 ID로 여러 번 조회해도 일관된 결과를 반환한다")
    void selectById_ConsistentResults() {
        // given
        Long userId = 1L;
        long amount = 10000L;
        userPointTable.insertOrUpdate(userId, amount);

        // when
        UserPoint firstQuery = userPointTable.selectById(userId);
        UserPoint secondQuery = userPointTable.selectById(userId);
        UserPoint thirdQuery = userPointTable.selectById(userId);

        // then
        assertThat(firstQuery).isEqualTo(secondQuery);
        assertThat(secondQuery).isEqualTo(thirdQuery);
        assertThat(firstQuery).isEqualTo(thirdQuery);
    }
} 