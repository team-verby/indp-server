package com.verby.indp.domain.payment.controller;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.payment.dto.response.FindOwnerPaymentsResponse;
import com.verby.indp.fixture.OwnerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OwnerPaymentControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/owner/payments/stores/{storeId} 실행 시")
    class FindPayments {

        @Test
        @DisplayName("성공 : 점주의 결제 내역 목록을 조회한다.")
        void findPayments() throws Exception {
            // given
            Owner owner = OwnerFixture.owner();
            givenOwnerAuth(owner);

            FindOwnerPaymentsResponse response = new FindOwnerPaymentsResponse(List.of(), 0, 0);
            given(ownerPaymentService.findPayments(any(), eq(1L), any())).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/owner/payments/stores/{storeId}", 1L)
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
}
