package com.verby.indp.domain.payment.controller;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.payment.PaymentType;
import com.verby.indp.domain.payment.dto.request.ConfirmPaymentRequest;
import com.verby.indp.domain.payment.dto.request.FailPaymentRequest;
import com.verby.indp.domain.payment.exception.PaymentFailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("POST /api/payments/confirm 실행 시")
    class ConfirmPayment {

        @Test
        @DisplayName("성공 : 구독 결제를 확인한다.")
        void confirmSubscriptionPayment() throws Exception {
            // given
            willDoNothing().given(paymentConfirmService).confirm(any());

            ConfirmPaymentRequest request = new ConfirmPaymentRequest(
                PaymentType.SUBSCRIPTION, "toss_payment_key_123", "INDP-20260101-uuid", 180000);

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/payments/confirm")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        requestFields(
                            fieldWithPath("paymentType").type(STRING)
                                .description("결제 유형 +\n`SUBSCRIPTION` (구독), `SONG_RECOMMENDATION` (노래 추천)"),
                            fieldWithPath("paymentKey").type(STRING)
                                .description("토스페이먼츠 paymentKey"),
                            fieldWithPath("orderId").type(STRING).description("주문 ID"),
                            fieldWithPath("amount").type(NUMBER).description("결제 금액 (원)")
                        )
                    )
                );
        }

        @Test
        @DisplayName("실패 : 결제 승인 실패 시 결제 상태를 ABORTED로 변경한다.")
        void confirmPaymentFail() throws Exception {
            // given
            ConfirmPaymentRequest request = new ConfirmPaymentRequest(
                PaymentType.SUBSCRIPTION, "toss_payment_key_123", "INDP-20260101-uuid", 180000);

            willThrow(new PaymentFailException("결제 승인에 실패하였습니다.")).given(paymentConfirmService).confirm(any());
            willDoNothing().given(paymentService).failPayment(any());

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/payments/confirm")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isServiceUnavailable());
        }
    }

    @Nested
    @DisplayName("POST /api/payments/fail 실행 시")
    class FailPayment {

        @Test
        @DisplayName("성공 : 결제 실패를 처리한다.")
        void failPayment() throws Exception {
            // given
            willDoNothing().given(paymentService).failPayment(any());

            FailPaymentRequest request = new FailPaymentRequest("INDP-20260101-uuid");

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/payments/fail")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        requestFields(
                            fieldWithPath("orderId").type(STRING).description("주문 ID")
                        )
                    )
                );
        }
    }
}
