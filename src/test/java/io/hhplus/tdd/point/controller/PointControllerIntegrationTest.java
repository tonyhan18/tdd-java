package io.hhplus.tdd.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.entity.PointHistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("PointController 통합 테스트")
class PointControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("포인트 충전/사용/거래내역/잔액조회 API가 정상 동작한다")
    void pointApi_basicUsage() throws Exception {
        long userId = 1L;
        long chargeAmount = 10000L;
        long useAmount = 3000L;

        // 1. 포인트 충전
        HashMap<String, Long> chargeRequest = new HashMap<>();
        chargeRequest.put("amount", chargeAmount);
        ResultActions chargeResult = mockMvc.perform(post("/point/{userId}/charge", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chargeRequest)));
        chargeResult.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(chargeAmount));

        // 2. 포인트 사용
        HashMap<String, Long> useRequest = new HashMap<>();
        useRequest.put("amount", useAmount);
        ResultActions useResult = mockMvc.perform(post("/point/{userId}/use", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(useRequest)));
        useResult.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(chargeAmount - useAmount));

        // 3. 거래내역 조회
        ResultActions historyResult = mockMvc.perform(get("/point/{userId}/histories", userId));
        historyResult.andExpect(status().isOk());
        String historyJson = historyResult.andReturn().getResponse().getContentAsString();
        List<PointHistory> histories = objectMapper.readerForListOf(PointHistory.class).readValue(historyJson);
        assertThat(histories).hasSize(2);

        // 4. 잔액 조회
        ResultActions balanceResult = mockMvc.perform(get("/point/{userId}", userId));
        balanceResult.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(chargeAmount - useAmount));
    }

    @Test
    @DisplayName("잘못된 금액 요청시 400 에러 반환")
    void invalidAmount_returnsBadRequest() throws Exception {
        long userId = 2L;
        HashMap<String, Long> badRequest = new HashMap<>();
        badRequest.put("amount", -100L);
        mockMvc.perform(post("/point/{userId}/charge", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/point/{userId}/use", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest());
    }
}
