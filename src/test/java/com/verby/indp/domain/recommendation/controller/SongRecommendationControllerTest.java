package com.verby.indp.domain.recommendation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.policy.PricePolicy;
import com.verby.indp.domain.recommendation.dto.request.RegisterSongRecommendationRequest;
import com.verby.indp.domain.recommendation.dto.response.RegisterSongRecommendationResponse;
import com.verby.indp.domain.store.dto.response.FindRecommendationFeeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.ResultActions;

class SongRecommendationControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("POST /api/stores/{storeId}/songs/recommendations 실행 시")
    class RegisterRecommendation {

        @Test
        @DisplayName("성공 : 노래 추천 주문을 생성한다.")
        void registerRecommendation() throws Exception {
            // given
            RegisterSongRecommendationResponse response = new RegisterSongRecommendationResponse(
                "INDP-REC-uuid", 500, "인디피_노래추천_카페공명");
            given(songRecommendationService.orderSongRecommendation(
                anyLong(), anyString(), anyString(), anyString(), anyInt(), anyString()))
                .willReturn(response);

            RegisterSongRecommendationRequest request = new RegisterSongRecommendationRequest(
                "성시경", "안녕 나의 사랑", "5zAEiu3SaO4", 259, "홍길동");

            // when
            ResultActions resultActions = mockMvc.perform(
                post("/api/stores/{storeId}/songs/recommendations", 1L)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isCreated())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("storeId").description("매장 ID")
                        ),
                        requestFields(
                            fieldWithPath("title").type(STRING).description("곡 제목"),
                            fieldWithPath("artist").type(STRING).description("아티스트"),
                            fieldWithPath("vid").type(STRING).description("YouTube 영상 ID"),
                            fieldWithPath("playTime").type(NUMBER).description("곡 길이 (초)"),
                            fieldWithPath("refereeName").type(STRING).description("추천자 이름")
                        ),
                        responseFields(
                            fieldWithPath("orderId").type(STRING).description("주문 ID"),
                            fieldWithPath("amount").type(NUMBER).description("결제 금액 (원)"),
                            fieldWithPath("orderName").type(STRING).description("주문명")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("GET /api/recommendation-fee 실행 시")
    class FindRecommendationFee {

        @Test
        @DisplayName("성공 : 노래 추천 수수료를 조회한다.")
        void findRecommendationFee() throws Exception {
            // given
            PricePolicy pricePolicy = Mockito.mock(PricePolicy.class);
            given(pricePolicy.getAmount()).willReturn(500);
            given(pricePolicyService.getByPolicyKey("recommendation_fee")).willReturn(pricePolicy);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/recommendation-fee"));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        responseFields(
                            fieldWithPath("amount").type(NUMBER).description("노래 추천 수수료 (원)")
                        )
                    )
                );
        }
    }
}
