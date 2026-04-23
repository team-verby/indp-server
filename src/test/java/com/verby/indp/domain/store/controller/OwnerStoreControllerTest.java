package com.verby.indp.domain.store.controller;

import static com.verby.indp.fixture.OwnerFixture.owner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.store.PlayMethod;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.request.BusinessHour;
import com.verby.indp.domain.store.dto.request.UpdateStoreRequest;
import com.verby.indp.domain.store.dto.response.FindLatestSubscriptionResponse;
import com.verby.indp.domain.store.dto.response.FindStoreByOwnerResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByOwnerResponse;
import com.verby.indp.domain.store.vo.PlaylistType;
import com.verby.indp.domain.store.vo.Tempo;
import com.verby.indp.domain.store.vo.Vibe;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import com.verby.indp.fixture.StoreFixture;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class OwnerStoreControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/owner/stores 실행 시")
    class FindMyStores {

        @Test
        @DisplayName("성공 : 점주의 매장 목록을 조회한다.")
        void findMyStores() throws Exception {
            // given
            Owner owner = owner();
            givenOwnerAuth(owner);

            FindStoresByOwnerResponse response = new FindStoresByOwnerResponse(List.of(), 1, 0);
            given(ownerStoreService.getMyStores(any())).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/owner/stores")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        responseFields(
                            fieldWithPath("stores").type(ARRAY).description("매장 목록"),
                            fieldWithPath("totalPages").type(NUMBER).description("전체 페이지 수"),
                            fieldWithPath("totalElements").type(NUMBER).description("전체 매장 수")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("GET /api/owner/stores/{storeId} 실행 시")
    class FindMyStore {

        @Test
        @DisplayName("성공 : 점주의 매장 상세를 조회한다.")
        void findMyStore() throws Exception {
            // given
            Owner owner = owner();
            givenOwnerAuth(owner);

            Store store = StoreFixture.store();
            FindStoreByOwnerResponse response = FindStoreByOwnerResponse.from(store);
            given(ownerStoreService.getMyStore(any(), eq(1L))).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/owner/stores/{storeId}", 1L)
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("storeId").description("매장 ID")
                        ),
                        responseFields(
                            fieldWithPath("applyInfo").type(OBJECT).description("신청 정보"),
                            fieldWithPath("applyInfo.applicantName").type(STRING)
                                .description("신청자 이름"),
                            fieldWithPath("applyInfo.applicantPhone").type(STRING)
                                .description("신청자 연락처"),
                            fieldWithPath("storeInfo").type(OBJECT).description("매장 정보"),
                            fieldWithPath("storeInfo.name").type(STRING).description("매장명"),
                            fieldWithPath("storeInfo.industry").type(STRING).description("업종"),
                            fieldWithPath("storeInfo.address").type(STRING).description("주소"),
                            fieldWithPath("storeInfo.customerAgeGroup").type(STRING)
                                .description("주요 고객 연령대"),
                            fieldWithPath("storeInfo.lighting").type(NUMBER)
                                .description("조명 밝기 (1~5)"),
                            fieldWithPath("storeInfo.vibes").type(ARRAY).description("매장 분위기 목록"),
                            fieldWithPath("storeInfo.photoUrls").type(ARRAY)
                                .description("매장 사진 URL 목록"),
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
                            fieldWithPath("musicInfo").type(OBJECT).description("음악 설정"),
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
                            fieldWithPath("musicInfo.musicMood").type(STRING)
                                .description("음악 분위기"),
                            fieldWithPath("musicInfo.musicTempo").type(STRING).description("음악 템포"),
                            fieldWithPath("musicInfo.preferredGenres").type(ARRAY)
                                .description("선호 장르 목록"),
                            fieldWithPath("musicInfo.rejectedGenres").type(ARRAY)
                                .description("차단 장르 목록"),
                            fieldWithPath("musicInfo.rejectedSongNote").type(STRING)
                                .description("제외 음악 메모")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("GET /api/owner/stores/{storeId}/subscription 실행 시")
    class FindLatestSubscription {

        @Test
        @DisplayName("성공 : 최신 구독 정보를 조회한다.")
        void findLatestSubscription() throws Exception {
            // given
            Owner owner = owner();
            givenOwnerAuth(owner);

            FindLatestSubscriptionResponse response = new FindLatestSubscriptionResponse(
                SubscriptionStatus.ACTIVE, "PLAN_A",
                LocalDate.of(2026, 1, 12), LocalDate.of(2027, 1, 12),
                LocalDateTime.of(2026, 1, 10, 14, 30),
                "ACTIVE"
            );
            given(ownerStoreService.getLatestSubscription(any(), eq(1L))).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(
                get("/api/owner/stores/{storeId}/subscription", 1L)
                    .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("storeId").description("매장 ID")
                        ),
                        responseFields(
                            fieldWithPath("status").type(STRING)
                                .description("구독 상태 (Enum 객체)"),
                            fieldWithPath("planType").type(STRING)
                                .description("플랜 종류 +\n`PLAN_A`, `PLAN_B`"),
                            fieldWithPath("startDate").type(STRING).description("구독 시작일"),
                            fieldWithPath("endDate").type(STRING).description("구독 종료일"),
                            fieldWithPath("paidAt").type(STRING).description("결제 완료 시각"),
                            fieldWithPath("subscriptionStatus").type(STRING)
                                .description("구독 상태 문자열 +\n`PENDING_PAYMENT`, `PENDING_ACTIVE`, `ACTIVE`, `EXPIRED`")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("PUT /api/owner/stores/{storeId} 실행 시")
    class UpdateStore {

        @Test
        @DisplayName("성공 : 매장 정보를 수정한다.")
        void updateStore() throws Exception {
            // given
            Owner owner = owner();
            givenOwnerAuth(owner);
            willDoNothing().given(ownerStoreService).updateStore(any(), eq(1L), any());

            UpdateStoreRequest request = new UpdateStoreRequest(
                "카페 공명 홍대점",
                "카페",
                "서울 마포구 와우산로17길 11-8",
                List.of(new BusinessHour(1, LocalTime.of(10, 0), LocalTime.of(22, 0), false)),
                List.of("https://example.com/photo.jpg"),
                "20대 중반 ~ 30대 초반",
                3,
                "유튜브 뮤직",
                "인디, 어쿠스틱",
                List.of(PlayMethod.Method.BLUETOOTH),
                List.of(Vibe.CALM),
                PlaylistType.CONSISTENT_MOOD,
                List.of(),
                Tempo.CALM,
                List.of(),
                "너무 빠른 비트 제외",
                "아늑하고 조용한"
            );

            // when
            ResultActions resultActions = mockMvc.perform(put("/api/owner/stores/{storeId}", 1L)
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isNoContent())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("storeId").description("매장 ID")
                        ),
                        requestFields(
                            fieldWithPath("name").type(STRING).description("매장명"),
                            fieldWithPath("industry").type(STRING).description("업종"),
                            fieldWithPath("address").type(STRING).description("주소"),
                            fieldWithPath("customerAgeGroup").type(STRING).description("주요 고객 연령대"),
                            fieldWithPath("lighting").type(NUMBER).description("조명 밝기 (1~5)"),
                            fieldWithPath("platform").type(STRING).description("음악 재생 플랫폼"),
                            fieldWithPath("playedMusic").type(STRING).description("주로 트는 음악"),
                            fieldWithPath("rejectedSongNote").type(STRING).description("제외 음악 메모"),
                            fieldWithPath("mood").type(STRING).description("매장 분위기 묘사"),
                            fieldWithPath("playlistType").type(STRING).description("플레이리스트 유형"),
                            fieldWithPath("musicTempo").type(STRING).description("음악 템포"),
                            fieldWithPath("playMethods").type(ARRAY).description("재생 방식 목록"),
                            fieldWithPath("vibes").type(ARRAY).description("매장 분위기 목록"),
                            fieldWithPath("businessHours").type(ARRAY).description("영업시간 목록"),
                            fieldWithPath("businessHours[].dayOfWeek").type(NUMBER)
                                .description("요일 (1=월 ~ 7=일)"),
                            fieldWithPath("businessHours[].openTime").type(STRING)
                                .description("영업 시작 시간"),
                            fieldWithPath("businessHours[].closeTime").type(STRING)
                                .description("영업 종료 시간"),
                            fieldWithPath("businessHours[].isClosed").type(BOOLEAN)
                                .description("휴무 여부"),
                            fieldWithPath("timePreferences").type(ARRAY).description("시간대별 무드 목록"),
                            fieldWithPath("preferenceGenres").type(ARRAY).description("선호/차단 장르"),
                            fieldWithPath("photoUrls").type(ARRAY).description("매장 사진 URL 목록")
                        )
                    )
                );
        }
    }
}
