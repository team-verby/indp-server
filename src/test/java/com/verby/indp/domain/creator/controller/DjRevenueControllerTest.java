package com.verby.indp.domain.creator.controller;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.response.DjRevenueResponse;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class DjRevenueControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/dj/revenue 실행 시")
    class GetRevenue {

        @Test
        @DisplayName("성공 : 정산 정보를 반환한다.")
        void getRevenue() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            given(djRevenueService.getRevenue(any())).willReturn(new DjRevenueResponse(
                100L, 60_000L, 0L, true, 50_000L, LocalDate.of(2026, 7, 15)));

            ResultActions resultActions = mockMvc.perform(get("/api/dj/revenue")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("thisMonthEstimate").type(NUMBER).description("당월 실시간 예상 적립액 (원)"),
                        fieldWithPath("balance").type(NUMBER).description("현재 적립 잔액 (원)"),
                        fieldWithPath("totalPaid").type(NUMBER).description("누적 지급액 (원)"),
                        fieldWithPath("canRequest").type(BOOLEAN).description("정산 신청 가능 여부 (잔액 ≥ 최소 신청 금액)"),
                        fieldWithPath("minPayout").type(NUMBER).description("최소 신청 금액 (원)"),
                        fieldWithPath("nextPayoutDate").type(STRING).description("다음 정산 지급 예정일")
                    )
                ));
        }
    }
}
