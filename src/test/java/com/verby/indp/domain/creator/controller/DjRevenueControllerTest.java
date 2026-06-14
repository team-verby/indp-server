package com.verby.indp.domain.creator.controller;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.response.DjRevenueResponse;
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
            given(djRevenueService.getRevenue(any())).willReturn(new DjRevenueResponse(null, null, null));

            ResultActions resultActions = mockMvc.perform(get("/api/dj/revenue")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("thisMonthEstimate").description("이번 달 예상 정산 (null 가능)"),
                        fieldWithPath("totalPaid").description("누적 정산 금액 (null 가능)"),
                        fieldWithPath("nextPayoutDate").description("다음 정산일 (null 가능)")
                    )
                ));
        }
    }
}
