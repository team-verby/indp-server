package com.verby.indp.domain.recommendation.controller;

import static com.verby.indp.domain.recommendation.fixture.RecommendationFixture.recommendation;
import static com.verby.indp.domain.region.fixture.RegionFixture.region;
import static com.verby.indp.domain.store.fixture.StoreFixture.store;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.recommendation.Recommendation;
import com.verby.indp.domain.recommendation.dto.request.RegisterRecommendationRequest;
import com.verby.indp.domain.region.Region;
import com.verby.indp.domain.store.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

class RecommendationControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 추천 음악 정보를 등록한다.")
    void registerRecommendation() throws Exception {
        // given
        Region 서울 = region("서울");
        Store store = store(서울);
        ReflectionTestUtils.setField(store, "storeId", 1L);
        Recommendation recommendation = recommendation(store(서울));

        RegisterRecommendationRequest request = new RegisterRecommendationRequest(store.getStoreId(),
            recommendation.getInformation(), recommendation.getPhoneNumber());

        when(recommendationService.registerRecommendation(request)).thenReturn(anyLong());

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/music/recommendations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().isCreated()).
            andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("storeId").type(NUMBER).description("매장 ID"),
                        fieldWithPath("information").type(STRING).description("추천 음악 정보"),
                        fieldWithPath("phoneNumber").type(STRING).description("추천인 연락처")
                    ),
                    responseHeaders(
                        headerWithName("Location").description("리소스 생성 위치")
                    )
                )
            );
    }

}
