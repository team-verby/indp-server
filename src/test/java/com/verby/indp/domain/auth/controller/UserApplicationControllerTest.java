package com.verby.indp.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.dto.request.UserApplicationRequest;
import com.verby.indp.domain.auth.dto.response.UserApplicationResponse;
import com.verby.indp.domain.common.exception.ConflictException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class UserApplicationControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("POST /api/user/applications 실행 시")
    class Apply {

        @Test
        @DisplayName("성공 : Plan A 구독을 신청하고 결제 정보를 반환한다.")
        void apply() throws Exception {
            given(userApplicationService.apply(any()))
                .willReturn(new UserApplicationResponse("order-uuid", 4400, "Plan A 라이트 요금제 · 월간"));

            UserApplicationRequest request = new UserApplicationRequest(
                "홍길동", "user@test.com", "password123!", 1);

            ResultActions resultActions = mockMvc.perform(post("/api/user/applications")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            resultActions.andExpect(status().isCreated())
                .andDo(restDocs.document(
                    requestFields(
                        fieldWithPath("name").type(STRING).description("성함"),
                        fieldWithPath("email").type(STRING).description("이메일 (로그인 ID)"),
                        fieldWithPath("password").type(STRING).description("비밀번호 (8자 이상)"),
                        fieldWithPath("usagePeriod").type(NUMBER).description("결제 주기 (1: 월간, 12: 연간)")
                    ),
                    responseFields(
                        fieldWithPath("orderId").type(STRING).description("Toss 결제 주문 ID"),
                        fieldWithPath("amount").type(NUMBER).description("결제 금액"),
                        fieldWithPath("orderName").type(STRING).description("주문명")
                    )
                ));
        }

        @Test
        @DisplayName("실패 : 이미 사용 중인 이메일이면 409를 반환한다.")
        void applyWithDuplicateEmail() throws Exception {
            willThrow(new ConflictException("이미 사용 중인 이메일입니다."))
                .given(userApplicationService).apply(any());

            UserApplicationRequest request = new UserApplicationRequest(
                "홍길동", "dup@test.com", "password123!", 1);

            ResultActions resultActions = mockMvc.perform(post("/api/user/applications")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            resultActions.andExpect(status().isConflict());
        }
    }
}
