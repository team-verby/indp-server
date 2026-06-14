package com.verby.indp.domain.auth.controller;

import static com.verby.indp.fixture.UserFixture.userWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.creator.dto.response.UserPaymentsResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;

class UserSubscriptionControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/user/subscription 실행 시")
    class GetSubscription {

        @Test
        @DisplayName("실패 : 구독 정보가 없으면 404를 반환한다.")
        void getSubscriptionNotFound() throws Exception {
            User user = userWithId(1L);
            given(authTokenService.decodeUserToken(any())).willReturn(1L);
            given(userRepository.findById(1L)).willReturn(Optional.of(user));
            willThrow(new NotFoundException("구독 정보가 없습니다."))
                .given(userSubscriptionService).getSubscription(any());

            ResultActions resultActions = mockMvc.perform(get("/api/user/subscription")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/user/subscription 성공 시")
    class GetSubscriptionSuccess {

        @Test
        @DisplayName("성공 : 구독 정보를 반환한다.")
        void getSubscriptionSuccess() throws Exception {
            User user = userWithId(1L);
            given(authTokenService.decodeUserToken(any())).willReturn(1L);
            given(userRepository.findById(1L)).willReturn(Optional.of(user));
            given(userSubscriptionService.getSubscription(any()))
                .willReturn(new com.verby.indp.domain.creator.dto.response.UserSubscriptionResponse(
                    "Plan A 라이트 요금제", 4400, 1,
                    java.time.LocalDate.of(2026, 6, 15),
                    java.time.LocalDate.of(2026, 7, 14)
                ));

            ResultActions resultActions = mockMvc.perform(get("/api/user/subscription")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("GET /api/user/payments 실행 시")
    class GetPayments {

        @Test
        @DisplayName("성공 : 빈 결제 내역을 반환한다.")
        void getPayments() throws Exception {
            User user = userWithId(1L);
            given(authTokenService.decodeUserToken(any())).willReturn(1L);
            given(userRepository.findById(1L)).willReturn(Optional.of(user));
            given(userSubscriptionService.getPayments(any()))
                .willReturn(new UserPaymentsResponse(List.of()));

            ResultActions resultActions = mockMvc.perform(get("/api/user/payments")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("payments").type(ARRAY).description("결제 내역 (미구현 - 빈 배열)")
                    )
                ));
        }
    }
}
