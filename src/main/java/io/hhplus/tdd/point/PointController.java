package io.hhplus.tdd.point;

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
        return new UserPoint(0, 0, 0);
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/use")
    public UserPoint use(
            @PathVariable long id,
            @RequestBody long amount
    ) {
        return new UserPoint(0, 0, 0);
    }
}
