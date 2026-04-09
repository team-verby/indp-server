package com.verby.indp.domain.plan.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.plan.dto.response.FindPlansResponse;
import com.verby.indp.fixture.PlanFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class PlanControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/plans 실행 시")
    class FindPlans {

        @Test
        @DisplayName("성공 : 플랜 목록을 조회한다.")
        void findPlans() throws Exception {
            // given
            Plan planA = PlanFixture.planA();
            Plan planB = PlanFixture.planB();

            FindPlansResponse response = FindPlansResponse.from(List.of(planA, planB));
            given(planService.getPlans()).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/plans"));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        responseFields(
                            fieldWithPath("plans").type(ARRAY).description("플랜 목록"),
                            fieldWithPath("plans[].planId").type(NUMBER).description("플랜 ID"),
                            fieldWithPath("plans[].type").type(STRING)
                                .description("플랜 종류 +\n`PLAN_A`, `PLAN_B`"),
                            fieldWithPath("plans[].monthlyPrice").type(NUMBER)
                                .description("월 기본 가격 (원)"),
                            fieldWithPath("plans[].discountRate").type(NUMBER)
                                .description("할인율 (%). 0이면 할인 없음")
                        )
                    )
                );
        }
    }
}
