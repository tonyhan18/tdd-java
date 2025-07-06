package io.hhplus.tdd.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.service.UserPointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserPointService userPointService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자 포인트 조회 - 성공")
    void getUserPoint_success() throws Exception {
        UserPoint userPoint = new UserPoint(1L, 10000L, System.currentTimeMillis());
        Mockito.when(userPointService.getUserPoint(1L)).thenReturn(userPoint);

        mockMvc.perform(get("/point/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.point").value(10000L));
    }

    @Test
    @DisplayName("포인트 거래 내역 조회 - 성공")
    void getTransactionHistory_success() throws Exception {
        PointHistory history = new PointHistory(1L, 1L, 1000L, null, System.currentTimeMillis());
        Mockito.when(userPointService.getTransactionHistory(1L)).thenReturn(List.of(history));

        mockMvc.perform(get("/point/1/histories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("포인트 충전 - 성공")
    void chargePoint_success() throws Exception {
        UserPoint userPoint = new UserPoint(1L, 20000L, System.currentTimeMillis());
        Mockito.when(userPointService.chargePoint(eq(1L), eq(10000L))).thenReturn(userPoint);

        mockMvc.perform(post("/point/1/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 10000L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(20000L));
    }

    @Test
    @DisplayName("포인트 충전 - 잘못된 요청")
    void chargePoint_invalidRequest() throws Exception {
        mockMvc.perform(post("/point/1/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", -100L))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("포인트 사용 - 성공")
    void usePoint_success() throws Exception {
        UserPoint userPoint = new UserPoint(1L, 5000L, System.currentTimeMillis());
        Mockito.when(userPointService.usePoint(eq(1L), eq(5000L))).thenReturn(userPoint);

        mockMvc.perform(post("/point/1/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 5000L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(5000L));
    }

    @Test
    @DisplayName("포인트 사용 - 잘못된 요청")
    void usePoint_invalidRequest() throws Exception {
        mockMvc.perform(post("/point/1/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 0L))))
                .andExpect(status().isBadRequest());
    }
} 