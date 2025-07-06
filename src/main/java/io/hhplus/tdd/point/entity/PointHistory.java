package io.hhplus.tdd.point.entity;

import io.hhplus.tdd.point.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "포인트 거래 내역")
public record PointHistory(
        @Schema(description = "거래 내역 ID", example = "1") long id,
        @Schema(description = "사용자 ID", example = "1") long userId,
        @Schema(description = "거래 금액", example = "10000") long amount,
        @Schema(description = "거래 타입", example = "CHARGE") TransactionType type,
        @Schema(description = "거래 시간 (밀리초)", example = "1703123456789") long updateMillis
) {
} 