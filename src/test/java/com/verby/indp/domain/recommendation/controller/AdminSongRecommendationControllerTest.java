package com.verby.indp.domain.recommendation.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.recommendation.dto.response.FindStoreRecommendationsResponse;
import com.verby.indp.fixture.AdminFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class AdminSongRecommendationControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/admin/stores/{storeId}/songs/recommendations 실행 시")
    class FindRecommendedSongs {

        @Test
        @DisplayName("성공 : 어드민이 매장의 노래 추천 목록을 조회한다.")
        void findRecommendedSongs() throws Exception {
            // given
            Admin admin = AdminFixture.admin();
            givenAdminAuth(admin);

            FindStoreRecommendationsResponse response = new FindStoreRecommendationsResponse(
                List.of());
            given(songRecommendationService.findRecommendedSongs(1L)).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(
                get("/api/admin/stores/{storeId}/songs/recommendations", 1L)
                    .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("storeId").description("매장 ID")
                        ),
                        responseFields(
                            fieldWithPath("recommendations").type(ARRAY).description("추천 목록")
                        )
                    )
                );
        }
    }
}
