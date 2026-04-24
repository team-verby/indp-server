package com.verby.indp.domain.payment.controller;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.payment.dto.request.CancelPaymentRequest;
import com.verby.indp.domain.payment.dto.response.FindAdminPaymentsResponse;
import com.verby.indp.fixture.AdminFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminPaymentControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/admin/payments/stores/{storeId} 실행 시")
    class FindPayments {

        @Test
        @DisplayName("성공 : 매장의 결제 내역 목록을 조회한다.")
        void findPayments() throws Exception {
            // given
            Admin admin = AdminFixture.admin();
            givenAdminAuth(admin);

            FindAdminPaymentsResponse response = new FindAdminPaymentsResponse(List.of(), 0, 0);
            given(adminPaymentService.findPayments(eq(1L), any())).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/admin/payments/stores/{storeId}", 1L)
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("storeId").description("매장 ID")
                        ),
                        responseFields(
                            fieldWithPath("payments").type(ARRAY).description("결제 내역 목록"),
                            fieldWithPath("totalPages").type(NUMBER).description("전체 페이지 수"),
                            fieldWithPath("totalElements").type(NUMBER).description("전체 결제 수")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("POST /api/admin/payments/{paymentId}/cancel 실행 시")
    class CancelPayment {

        @Test
        @DisplayName("성공 : 결제를 취소한다.")
        void cancelPayment() throws Exception {
            // given
            Admin admin = AdminFixture.admin();
            givenAdminAuth(admin);

            willDoNothing().given(adminPaymentService).cancelPayment(eq(1L), any());

            CancelPaymentRequest request = new CancelPaymentRequest(180000, "단순 변심");

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/admin/payments/{paymentId}/cancel", 1L)
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("paymentId").description("결제 ID")
                        ),
                        requestFields(
                            fieldWithPath("cancelAmount").type(NUMBER).description("취소 금액 (원)"),
                            fieldWithPath("cancelReason").type(STRING).description("취소 사유")
                        )
                    )
                );
        }
    }
}
