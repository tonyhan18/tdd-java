package io.hhplus.tdd.point.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 포인트 트랜잭션 종류
 * - CHARGE : 충전
 * - USE : 사용
 */
@Schema(description = "포인트 거래 타입")
public enum TransactionType {
    @Schema(description = "포인트 충전") CHARGE, 
    @Schema(description = "포인트 사용") USE
} 