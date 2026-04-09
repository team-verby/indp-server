package com.verby.indp.domain.store.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.response.FindStoreByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByAdminResponse;
import com.verby.indp.fixture.AdminFixture;
import com.verby.indp.fixture.StoreFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class AdminStoreControllerTest extends BaseControllerTest {

    private Admin admin() {
        return AdminFixture.admin();
    }

    @Nested
    @DisplayName("GET /api/admin/stores 실행 시")
    class FindStores {

        @Test
        @DisplayName("성공 : 어드민이 매장 목록을 조회한다.")
        void findStores() throws Exception {
            // given
            Admin admin = admin();
            givenAdminAuth(admin);

            FindStoresByAdminResponse response = new FindStoresByAdminResponse(List.of());
            given(adminStoreService.findStores(any())).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/admin/stores")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        responseFields(
                            fieldWithPath("stores").type(ARRAY).description("매장 목록")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("GET /api/admin/stores/{storeId} 실행 시")
    class FindStore {

        @Test
        @DisplayName("성공 : 어드민이 매장 상세를 조회한다.")
        void findStore() throws Exception {
            // given
            Admin admin = admin();
            givenAdminAuth(admin);

            Store store = StoreFixture.store();
            FindStoreByAdminResponse response = FindStoreByAdminResponse.from(store);
            given(adminStoreService.findStore(1L)).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/admin/stores/{storeId}", 1L)
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("storeId").description("매장 ID")
                        ),
                        responseFields(
                            fieldWithPath("applyInfo.applicantName").type(STRING)
                                .description("신청자 이름"),
                            fieldWithPath("applyInfo.applicantPhone").type(STRING)
                                .description("신청자 연락처"),
                            fieldWithPath("storeInfo.name").type(STRING).description("매장명"),
                            fieldWithPath("storeInfo.industry").type(STRING).description("업종"),
                            fieldWithPath("storeInfo.address").type(STRING).description("주소"),
                            fieldWithPath("storeInfo.customerAgeGroup").type(STRING)
                                .description("주요 고객 연령대"),
                            fieldWithPath("storeInfo.lighting").type(NUMBER)
                                .description("조명 밝기 (1~5)"),
                            fieldWithPath("storeInfo.vibes").type(ARRAY).description("분위기 목록"),
                            fieldWithPath("storeInfo.photoUrls").type(ARRAY).description("사진 URL 목록"),
                            fieldWithPath("storeInfo.businessHours").type(ARRAY)
                                .description("영업시간 목록"),
                            fieldWithPath("storeInfo.businessHours[].dayOfWeek").type(NUMBER)
                                .description("요일 (1=월 ~ 7=일)"),
                            fieldWithPath("storeInfo.businessHours[].openTime").type(STRING)
                                .description("영업 시작 시간"),
                            fieldWithPath("storeInfo.businessHours[].closeTime").type(STRING)
                                .description("영업 종료 시간"),
                            fieldWithPath("storeInfo.businessHours[].isClosed").type(BOOLEAN)
                                .description("휴무 여부"),
                            fieldWithPath("musicInfo.playMethods").type(ARRAY)
                                .description("재생 방식 목록"),
                            fieldWithPath("musicInfo.playlistType").type(STRING)
                                .description("플레이리스트 유형"),
                            fieldWithPath("musicInfo.timePreferences").type(ARRAY)
                                .description("시간대별 무드 목록"),
                            fieldWithPath("musicInfo.MusicPlatform").type(STRING)
                                .description("음악 플랫폼"),
                            fieldWithPath("musicInfo.playedMusic").type(STRING)
                                .description("주로 트는 음악"),
                            fieldWithPath("musicInfo.musicMood").type(STRING)
                                .description("음악 분위기"),
                            fieldWithPath("musicInfo.musicTempo").type(STRING).description("음악 템포"),
                            fieldWithPath("musicInfo.rejectedGenres").type(ARRAY)
                                .description("차단 장르 목록"),
                            fieldWithPath("subscriptionInfo.planType").type(STRING)
                                .description("플랜 종류"),
                            fieldWithPath("subscriptionInfo.startDate").type(STRING)
                                .description("구독 시작일"),
                            fieldWithPath("subscriptionInfo.endTime").type(STRING)
                                .description("구독 종료일"),
                            fieldWithPath("subscriptionInfo.status").type(STRING).description("구독 상태")
                        )
                    )
                );
        }
    }
}
