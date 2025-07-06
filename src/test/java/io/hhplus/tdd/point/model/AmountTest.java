package io.hhplus.tdd.point.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Amount 모델 테스트")
class AmountTest {

    @Test
    @DisplayName("정상적인 금액으로 Amount 객체를 생성할 수 있다")
    void createAmountWithValidValue() {
        // given
        long validAmount = 10000L;

        // when
        Amount amount = new Amount(validAmount);

        // then
        assertThat(amount.getValue()).isEqualTo(validAmount);
    }

    @Test
    @DisplayName("0원으로 Amount 객체를 생성할 수 있다")
    void createAmountWithZero() {
        // when
        Amount amount = new Amount(0L);

        // then
        assertThat(amount.getValue()).isEqualTo(0L);
        assertThat(amount.isZero()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -100L, -1000L})
    @DisplayName("음수 금액으로 Amount 객체를 생성하면 예외가 발생한다")
    void createAmountWithNegativeValue_ThrowsException(long negativeAmount) {
        // when & then
        assertThatThrownBy(() -> new Amount(negativeAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("금액은 0 이상이어야 합니다");
    }

    @Test
    @DisplayName("최대값을 초과하는 금액으로 Amount 객체를 생성하면 예외가 발생한다")
    void createAmountWithExceedMaxValue_ThrowsException() {
        // given
        long exceedAmount = Amount.MAX_AMOUNT + 1L;

        // when & then
        assertThatThrownBy(() -> new Amount(exceedAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("금액은 " + Amount.MAX_AMOUNT + "을 초과할 수 없습니다");
    }

    @Test
    @DisplayName("isZero() 메서드가 정상적으로 동작한다")
    void isZero_ReturnsCorrectValue() {
        // given
        Amount zeroAmount = new Amount(0L);
        Amount nonZeroAmount = new Amount(1000L);

        // when & then
        assertThat(zeroAmount.isZero()).isTrue();
        assertThat(nonZeroAmount.isZero()).isFalse();
    }

    @Test
    @DisplayName("isPositive() 메서드가 정상적으로 동작한다")
    void isPositive_ReturnsCorrectValue() {
        // given
        Amount zeroAmount = new Amount(0L);
        Amount positiveAmount = new Amount(1000L);

        // when & then
        assertThat(zeroAmount.isPositive()).isFalse();
        assertThat(positiveAmount.isPositive()).isTrue();
    }

    @Test
    @DisplayName("두 Amount 객체를 더할 수 있다")
    void add_ReturnsCorrectResult() {
        // given
        Amount amount1 = new Amount(5000L);
        Amount amount2 = new Amount(3000L);

        // when
        Amount result = amount1.add(amount2);

        // then
        assertThat(result.getValue()).isEqualTo(8000L);
    }

    @Test
    @DisplayName("0원과 다른 금액을 더하면 원래 금액이 반환된다")
    void addWithZero_ReturnsOriginalAmount() {
        // given
        Amount zeroAmount = new Amount(0L);
        Amount amount = new Amount(5000L);

        // when
        Amount result = amount.add(zeroAmount);

        // then
        assertThat(result.getValue()).isEqualTo(5000L);
    }

    @Test
    @DisplayName("Amount 객체에서 다른 Amount 객체를 뺄 수 있다")
    void subtract_ReturnsCorrectResult() {
        // given
        Amount amount1 = new Amount(10000L);
        Amount amount2 = new Amount(3000L);

        // when
        Amount result = amount1.subtract(amount2);

        // then
        assertThat(result.getValue()).isEqualTo(7000L);
    }

    @Test
    @DisplayName("같은 금액을 빼면 0원이 된다")
    void subtractSameAmount_ReturnsZero() {
        // given
        Amount amount = new Amount(5000L);

        // when
        Amount result = amount.subtract(amount);

        // then
        assertThat(result.getValue()).isEqualTo(0L);
        assertThat(result.isZero()).isTrue();
    }

    @Test
    @DisplayName("더 큰 금액을 빼면 예외가 발생한다")
    void subtractLargerAmount_ThrowsException() {
        // given
        Amount smallerAmount = new Amount(3000L);
        Amount largerAmount = new Amount(5000L);

        // when & then
        assertThatThrownBy(() -> smallerAmount.subtract(largerAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잔액이 부족합니다");
    }

    @Test
    @DisplayName("isGreaterThanOrEqual() 메서드가 정상적으로 동작한다")
    void isGreaterThanOrEqual_ReturnsCorrectValue() {
        // given
        Amount amount1 = new Amount(5000L);
        Amount amount2 = new Amount(3000L);
        Amount amount3 = new Amount(5000L);

        // when & then
        assertThat(amount1.isGreaterThanOrEqual(amount2)).isTrue();  // 더 큰 경우
        assertThat(amount1.isGreaterThanOrEqual(amount3)).isTrue();  // 같은 경우
        assertThat(amount2.isGreaterThanOrEqual(amount1)).isFalse(); // 더 작은 경우
    }

    @Test
    @DisplayName("isGreaterThan() 메서드가 정상적으로 동작한다")
    void isGreaterThan_ReturnsCorrectValue() {
        // given
        Amount amount1 = new Amount(5000L);
        Amount amount2 = new Amount(3000L);
        Amount amount3 = new Amount(5000L);

        // when & then
        assertThat(amount1.isGreaterThan(amount2)).isTrue();  // 더 큰 경우
        assertThat(amount1.isGreaterThan(amount3)).isFalse(); // 같은 경우
        assertThat(amount2.isGreaterThan(amount1)).isFalse(); // 더 작은 경우
    }

    @Test
    @DisplayName("format() 메서드가 천 단위 콤마를 포함한 문자열을 반환한다")
    void format_ReturnsFormattedString() {
        // given
        Amount amount = new Amount(1234567L);

        // when
        String formatted = amount.format();

        // then
        assertThat(formatted).isEqualTo("1,234,567");
    }

    @Test
    @DisplayName("0원의 format() 메서드가 정상적으로 동작한다")
    void formatZero_ReturnsZero() {
        // given
        Amount zeroAmount = new Amount(0L);

        // when
        String formatted = zeroAmount.format();

        // then
        assertThat(formatted).isEqualTo("0");
    }

    @Test
    @DisplayName("zero() 정적 메서드가 0원 Amount를 반환한다")
    void zero_ReturnsZeroAmount() {
        // when
        Amount zeroAmount = Amount.zero();

        // then
        assertThat(zeroAmount.getValue()).isEqualTo(0L);
        assertThat(zeroAmount.isZero()).isTrue();
    }

    @Test
    @DisplayName("of() 정적 메서드가 올바른 Amount를 반환한다")
    void of_ReturnsCorrectAmount() {
        // given
        long value = 15000L;

        // when
        Amount amount = Amount.of(value);

        // then
        assertThat(amount.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("equals() 메서드가 정상적으로 동작한다")
    void equals_ReturnsCorrectValue() {
        // given
        Amount amount1 = new Amount(5000L);
        Amount amount2 = new Amount(5000L);
        Amount amount3 = new Amount(3000L);

        // when & then
        assertThat(amount1).isEqualTo(amount2);
        assertThat(amount1).isNotEqualTo(amount3);
        assertThat(amount1).isEqualTo(amount1); // 자기 자신과 비교
    }

    @Test
    @DisplayName("hashCode() 메서드가 정상적으로 동작한다")
    void hashCode_ReturnsCorrectValue() {
        // given
        Amount amount1 = new Amount(5000L);
        Amount amount2 = new Amount(5000L);

        // when & then
        assertThat(amount1.hashCode()).isEqualTo(amount2.hashCode());
    }

    @Test
    @DisplayName("toString() 메서드가 포맷팅된 문자열을 반환한다")
    void toString_ReturnsFormattedString() {
        // given
        Amount amount = new Amount(10000L);

        // when
        String result = amount.toString();

        // then
        assertThat(result).isEqualTo("Amount{value=10,000}");
    }

    @Test
    @DisplayName("최대값으로 Amount 객체를 생성할 수 있다")
    void createAmountWithMaxValue() {
        // when
        Amount maxAmount = new Amount(Amount.MAX_AMOUNT);

        // then
        assertThat(maxAmount.getValue()).isEqualTo(Amount.MAX_AMOUNT);
        assertThat(maxAmount.isPositive()).isTrue();
    }

    @Test
    @DisplayName("복잡한 계산이 정상적으로 동작한다")
    void complexCalculation_WorksCorrectly() {
        // given
        Amount initialAmount = new Amount(10000L);
        Amount chargeAmount = new Amount(5000L);
        Amount useAmount = new Amount(3000L);

        // when
        Amount afterCharge = initialAmount.add(chargeAmount);
        Amount afterUse = afterCharge.subtract(useAmount);

        // then
        assertThat(afterCharge.getValue()).isEqualTo(15000L);
        assertThat(afterUse.getValue()).isEqualTo(12000L);
        assertThat(afterUse.isGreaterThan(initialAmount)).isTrue();
    }
} 