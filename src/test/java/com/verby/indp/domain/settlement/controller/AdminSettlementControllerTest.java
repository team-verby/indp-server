package com.verby.indp.domain.settlement.controller;

import static com.verby.indp.fixture.AdminFixture.admin;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.settlement.SettlementStatus;
import com.verby.indp.domain.settlement.dto.response.FindSettlementsResponse;
import com.verby.indp.domain.settlement.dto.response.FindSettlementsResponse.SettlementItem;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class AdminSettlementControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/admin/settlements 실행 시")
    class FindSettlements {

        @Test
        @DisplayName("성공 : 정산 신청 목록을 반환한다.")
        void findSettlements() throws Exception {
            givenAdminAuth(admin());
            FindSettlementsResponse response = new FindSettlementsResponse(List.of(
                new SettlementItem(
                    1L, 10L, "DJ Parkwan", 60_000L, SettlementStatus.PAID,
                    LocalDateTime.of(2026, 6, 18, 10, 0), LocalDateTime.of(2026, 6, 19, 9, 0))));
            given(adminSettlementService.findSettlements(any())).willReturn(response);

            ResultActions resultActions = mockMvc.perform(get("/api/admin/settlements")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("settlements[].settlementRequestId").type(NUMBER).description("정산 신청 ID"),
                        fieldWithPath("settlements[].creatorId").type(NUMBER).description("크리에이터 ID"),
                        fieldWithPath("settlements[].djName").type(STRING).description("DJ(채널)명"),
                        fieldWithPath("settlements[].amount").type(NUMBER).description("신청 금액 (원)"),
                        fieldWithPath("settlements[].status").type(STRING).description("상태 (REQUESTED/PAID/REJECTED)"),
                        fieldWithPath("settlements[].requestedAt").type(STRING).description("신청 일시"),
                        fieldWithPath("settlements[].processedAt").type(STRING).optional().description("처리 일시 (미처리 시 null)")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("PATCH /api/admin/settlements/{id}/approve 실행 시")
    class Approve {

        @Test
        @DisplayName("성공 : 정산 신청을 지급 처리한다.")
        void approve() throws Exception {
            givenAdminAuth(admin());
            willDoNothing().given(adminSettlementService).approve(anyLong());

            ResultActions resultActions = mockMvc.perform(patch("/api/admin/settlements/{id}/approve", 1L)
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("PATCH /api/admin/settlements/{id}/reject 실행 시")
    class Reject {

        @Test
        @DisplayName("성공 : 정산 신청을 반려한다.")
        void reject() throws Exception {
            givenAdminAuth(admin());
            willDoNothing().given(adminSettlementService).reject(anyLong());

            ResultActions resultActions = mockMvc.perform(patch("/api/admin/settlements/{id}/reject", 1L)
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk());
        }
    }
}
