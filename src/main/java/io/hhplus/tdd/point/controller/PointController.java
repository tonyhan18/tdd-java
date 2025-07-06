package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.model.Amount;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point")
@Tag(name = "포인트 관리", description = "사용자 포인트 조회, 충전, 사용 API")
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}")
    @Operation(summary = "사용자 포인트 조회", description = "특정 사용자의 현재 포인트 잔액을 조회합니다.")
    public UserPoint point(
            @Parameter(description = "사용자 ID", example = "1") @PathVariable long id
    ) {
        return new UserPoint(0, 0, 0);
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}/histories")
    @Operation(summary = "포인트 내역 조회", description = "특정 사용자의 포인트 충전/사용 내역을 조회합니다.")
    public List<PointHistory> history(
            @Parameter(description = "사용자 ID", example = "1") @PathVariable long id
    ) {
        return List.of();
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/charge")
    @Operation(summary = "포인트 충전", description = "특정 사용자의 포인트를 충전합니다.")
    public UserPoint charge(
            @Parameter(description = "사용자 ID", example = "1") @PathVariable long id,
            @Parameter(description = "충전할 포인트 금액", example = "10000") @RequestBody long amount
    ) {
        // Amount 모델을 사용하여 금액 유효성 검증
        Amount chargeAmount = Amount.of(amount);
        log.info("포인트 충전 요청 - 사용자 ID: {}, 충전 금액: {}", id, chargeAmount.format());
        
        // TODO: 실제 비즈니스 로직 구현
        return new UserPoint(0, 0, 0);
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/use")
    @Operation(summary = "포인트 사용", description = "특정 사용자의 포인트를 사용합니다.")
    public UserPoint use(
            @Parameter(description = "사용자 ID", example = "1") @PathVariable long id,
            @Parameter(description = "사용할 포인트 금액", example = "5000") @RequestBody long amount
    ) {
        // Amount 모델을 사용하여 금액 유효성 검증
        Amount useAmount = Amount.of(amount);
        log.info("포인트 사용 요청 - 사용자 ID: {}, 사용 금액: {}", id, useAmount.format());
        
        // TODO: 실제 비즈니스 로직 구현
        // 예시: 잔액 확인 로직
        // Amount currentBalance = Amount.of(currentUserPoint.point());
        // if (!currentBalance.isGreaterThanOrEqual(useAmount)) {
        //     throw new IllegalArgumentException("잔액이 부족합니다.");
        // }
        
        return new UserPoint(0, 0, 0);
    }
} 