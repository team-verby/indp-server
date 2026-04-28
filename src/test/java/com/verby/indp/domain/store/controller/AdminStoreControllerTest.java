package com.verby.indp.domain.store.controller;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.request.TimePreference;
import com.verby.indp.domain.store.dto.request.UpdateTimePreferencesByAdminRequest;
import com.verby.indp.domain.store.dto.response.FindStoreByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByAdminResponse.StoreItem;
import com.verby.indp.fixture.AdminFixture;
import com.verby.indp.fixture.StoreFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

            Store store = StoreFixture.store();
            FindStoresByAdminResponse response = new FindStoresByAdminResponse(
                List.of(StoreItem.from(store, null)), 1, 1);
            given(adminStoreService.findStores(any())).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/admin/stores")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        responseFields(
                            fieldWithPath("stores").type(ARRAY).description("매장 목록"),
                            fieldWithPath("stores[].storeId").type(NUMBER).description("매장 ID"),
                            fieldWithPath("stores[].name").type(STRING).description("매장명"),
                            fieldWithPath("stores[].subscriptions").type(ARRAY)
                                .description("활성 구독 목록 (ACTIVE, PENDING_ACTIVE)"),
                            fieldWithPath("stores[].subscriptions[].plan").type(STRING)
                                .description("플랜 종류"),
                            fieldWithPath("stores[].subscriptions[].startDate").type(STRING)
                                .description("구독 시작일"),
                            fieldWithPath("stores[].subscriptions[].endDate").type(STRING)
                                .description("구독 종료일"),
                            fieldWithPath("stores[].subscriptions[].status").type(STRING)
                                .description("구독 상태"),
                            fieldWithPath("stores[].currentSong").type(NULL)
                                .description("현재 재생 중인 곡 (없으면 null)").optional(),
                            fieldWithPath("totalPages").type(NUMBER).description("전체 페이지 수"),
                            fieldWithPath("totalElements").type(NUMBER).description("전체 매장 수")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("PATCH /api/admin/stores/{storeId}/time-preferences 실행 시")
    class UpdateTimePreferences {

        @Test
        @DisplayName("성공 : 어드민이 매장의 시간대별 무드를 수정한다.")
        void updateTimePreferences() throws Exception {
            // given
            Admin admin = admin();
            givenAdminAuth(admin);

            UpdateTimePreferencesByAdminRequest request = new UpdateTimePreferencesByAdminRequest(
                List.of(new TimePreference(
                    java.time.LocalTime.of(10, 0), java.time.LocalTime.of(14, 0), "아늑하고 조용한"))
            );
            willDoNothing().given(adminStoreService).updateTimePreferences(eq(1L), any());

            // when
            ResultActions resultActions = mockMvc.perform(
                patch("/api/admin/stores/{storeId}/time-preferences", 1L)
                    .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isNoContent())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("storeId").description("매장 ID")
                        ),
                        requestFields(
                            fieldWithPath("timePreferences").type(ARRAY)
                                .description("수정할 시간대 무드 목록"),
                            fieldWithPath("timePreferences[].startTime").type(STRING)
                                .description("시작 시간 (HH:mm:ss)"),
                            fieldWithPath("timePreferences[].endTime").type(STRING)
                                .description("종료 시간 (HH:mm:ss)"),
                            fieldWithPath("timePreferences[].mood").type(STRING)
                                .description("변경할 무드")
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
                            fieldWithPath("musicInfo.timePreferences[].startTimeHour").type(NUMBER)
                                .description("시작 시각"),
                            fieldWithPath("musicInfo.timePreferences[].endTimeHour").type(NUMBER)
                                .description("종료 시각"),
                            fieldWithPath("musicInfo.timePreferences[].mood").type(STRING)
                                .description("해당 시간대 무드"),
                            fieldWithPath("musicInfo.MusicPlatform").type(STRING)
                                .description("음악 플랫폼"),
                            fieldWithPath("musicInfo.playedMusic").type(STRING)
                                .description("주로 트는 음악"),
                            fieldWithPath("musicInfo.musicMood").type(STRING)
                                .description("음악 분위기"),
                            fieldWithPath("musicInfo.musicTempo").type(STRING).description("음악 템포"),
                            fieldWithPath("musicInfo.rejectedGenres").type(ARRAY)
                                .description("차단 장르 목록"),
                            fieldWithPath("subscriptions").type(ARRAY)
                                .description("활성 구독 목록 (ACTIVE, PENDING_ACTIVE)"),
                            fieldWithPath("subscriptions[].planType").type(STRING)
                                .description("플랜 종류"),
                            fieldWithPath("subscriptions[].startDate").type(STRING)
                                .description("구독 시작일"),
                            fieldWithPath("subscriptions[].endDate").type(STRING)
                                .description("구독 종료일"),
                            fieldWithPath("subscriptions[].status").type(STRING).description("구독 상태")
                        )
                    )
                );
        }
    }
}
