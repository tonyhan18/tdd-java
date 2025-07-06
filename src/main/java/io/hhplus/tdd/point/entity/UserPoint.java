package io.hhplus.tdd.point.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 포인트 정보")
public record UserPoint(
        @Schema(description = "사용자 ID", example = "1") long id,
        @Schema(description = "현재 포인트 잔액", example = "15000") long point,
        @Schema(description = "마지막 업데이트 시간 (밀리초)", example = "1703123456789") long updateMillis
) {

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }
} 