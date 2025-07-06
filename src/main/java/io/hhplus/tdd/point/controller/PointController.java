package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.service.UserPointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 포인트 관련 REST API 컨트롤러
 */
@RestController
@RequestMapping("/point")
@Tag(name = "Point", description = "포인트 관리 API")
public class PointController {

    private final UserPointService userPointService;

    public PointController(UserPointService userPointService) {
        this.userPointService = userPointService;
    }
    /**
     * 사용자 포인트 조회
     */
    @GetMapping("/{userId}")
    @Operation(summary = "사용자 포인트 조회", description = "특정 사용자의 포인트 정보를 조회합니다.")
    public ResponseEntity<UserPoint> getUserPoint(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable long userId) {
        UserPoint userPoint = userPointService.getUserPoint(userId);
        return ResponseEntity.ok(userPoint);
    }

    /**
     * 포인트 거래 내역 조회
     */
    @GetMapping("/{userId}/histories")
    @Operation(summary = "포인트 거래 내역 조회", description = "사용자의 포인트 거래 내역을 조회합니다.")
    public ResponseEntity<List<PointHistory>> history(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable long userId) {
        
        List<PointHistory> history = userPointService.getTransactionHistory(userId);
        return ResponseEntity.ok(history);
    }

    /**
     * 포인트 충전
     */
    @PostMapping("/{userId}/charge")
    @Operation(summary = "포인트 충전", description = "사용자의 포인트를 충전합니다.")
    public ResponseEntity<UserPoint> chargePoint(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable long userId,
            @Parameter(description = "충전할 금액", example = "10000")
            @RequestBody Map<String, Long> request) {
        
        Long chargeAmount = request.get("amount");
        if (chargeAmount == null || chargeAmount <= 0) {
            return ResponseEntity.badRequest().build();
        }
        
        UserPoint chargedUserPoint = userPointService.chargePoint(userId, chargeAmount);
        return ResponseEntity.ok(chargedUserPoint);
    }

    /**
     * 포인트 사용
     */
    @PostMapping("/{userId}/use")
    @Operation(summary = "포인트 사용", description = "사용자의 포인트를 사용합니다.")
    public ResponseEntity<UserPoint> usePoint(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable long userId,
            @Parameter(description = "사용할 금액", example = "5000")
            @RequestBody Map<String, Long> request) {
        
        Long useAmount = request.get("amount");
        if (useAmount == null || useAmount <= 0) {
            return ResponseEntity.badRequest().build();
        }
        
        UserPoint usedUserPoint = userPointService.usePoint(userId, useAmount);
        return ResponseEntity.ok(usedUserPoint);
    }
} 