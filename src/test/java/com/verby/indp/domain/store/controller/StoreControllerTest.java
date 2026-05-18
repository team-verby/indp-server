package com.verby.indp.domain.store.controller;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.store.MusicGenre;
import com.verby.indp.domain.store.PlayMethod;
import com.verby.indp.domain.store.dto.request.ApplyStoreRequest;
import com.verby.indp.domain.store.dto.request.BusinessHour;
import com.verby.indp.domain.store.dto.request.GenreItem;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.response.AddFirstSubscriptionResponse;
import com.verby.indp.domain.store.dto.response.FindStoreSummaryResponse;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
import com.verby.indp.domain.store.vo.Genre;
import com.verby.indp.domain.store.vo.PlaylistType;
import com.verby.indp.domain.store.vo.Tempo;
import com.verby.indp.domain.store.vo.Vibe;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.fixture.OwnerFixture;
import com.verby.indp.fixture.StoreFixture;
import com.verby.indp.fixture.StoreSubscriptionFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalTime;
import java.util.List;

import static com.verby.indp.fixture.OwnerFixture.owner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StoreControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("POST /api/stores/applications 실행 시")
    class ApplyStore {

        @Test
        @DisplayName("성공 : 매장을 신청한다.")
        void applyStore() throws Exception {
            // given
            Store store = StoreFixture.storeWithOwner(OwnerFixture.owner());
            StoreSubscription subscription = StoreSubscriptionFixture.activeSubscription();
            subscription.setStore(store);
            AddFirstSubscriptionResponse response = AddFirstSubscriptionResponse.from(subscription);
            given(applyStoreService.applyStore(any())).willReturn(response);

            ApplyStoreRequest request = new ApplyStoreRequest(
                "홍길동",
                "010-1234-5678",
                1L,
                12,
                "카페 공명 홍대점",
                "카페",
                "서울 마포구 와우산로17길 11-8",
                List.of(new BusinessHour(1, LocalTime.of(10, 0), LocalTime.of(22, 0), false)),
                List.of("https://example.com/photo.jpg"),
                "유튜브 뮤직",
                "인디, 어쿠스틱",
                "20대 중반 ~ 30대 초반",
                List.of(PlayMethod.Method.BLUETOOTH),
                List.of(Vibe.CALM, Vibe.NATURAL),
                3,
                PlaylistType.CONSISTENT_MOOD,
                List.of(),
                Tempo.CALM,
                List.of(new GenreItem(Genre.INDIE, MusicGenre.PreferenceType.LIKE)),
                "너무 빠른 비트 제외",
                "아늑하고 조용한"
            );

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/stores/applications")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isCreated())
                .andDo(
                    restDocs.document(
                        requestFields(
                            fieldWithPath("applicantName").type(STRING).description("신청자 이름"),
                            fieldWithPath("applicantPhone").type(STRING).description("신청자 연락처"),
                            fieldWithPath("planId").type(NUMBER).description("구독할 플랜 ID"),
                            fieldWithPath("usagePeriod").type(NUMBER).description("구독 개월 수"),
                            fieldWithPath("name").type(STRING).description("매장명 (중복 불가)"),
                            fieldWithPath("industry").type(STRING).description("업종"),
                            fieldWithPath("address").type(STRING).description("매장 주소"),
                            fieldWithPath("customerAgeGroup").type(STRING).description("주요 고객 연령대"),
                            fieldWithPath("lighting").type(NUMBER).description("조명 밝기 (1~5)"),
                            fieldWithPath("platform").type(STRING).description("음악 재생 플랫폼"),
                            fieldWithPath("playedMusic").type(STRING).description("주로 트는 음악 설명"),
                            fieldWithPath("rejectedSongNote").type(STRING).description("제외 음악 메모"),
                            fieldWithPath("mood").type(STRING).description("매장 분위기 묘사"),
                            fieldWithPath("playlistType").type(STRING)
                                .description("플레이리스트 유형 +\n`CONSISTENT_MOOD`, `TIME_BASED`, `MUSIC_RECOMMENDED`"),
                            fieldWithPath("musicTempo").type(STRING)
                                .description("음악 템포 +\n`SLOW`, `CALM`, `NORMAL`, `LIVELY`, `UPBEAT`"),
                            fieldWithPath("playMethods").type(ARRAY)
                                .description("재생 방식 +\n`BLUETOOTH`, `WIRED`, `OTHER`"),
                            fieldWithPath("vibes").type(ARRAY)
                                .description("매장 분위기 +\n`CALM`, `MODERN`, `ELEGANT`, `DARK`, `NATURAL`, `OTHER`"),
                            fieldWithPath("businessHours").type(ARRAY).description("영업시간 목록"),
                            fieldWithPath("businessHours[].dayOfWeek").type(NUMBER)
                                .description("요일 (1=월 ~ 7=일)"),
                            fieldWithPath("businessHours[].openTime").type(STRING)
                                .description("영업 시작 시간"),
                            fieldWithPath("businessHours[].closeTime").type(STRING)
                                .description("영업 종료 시간"),
                            fieldWithPath("businessHours[].isClosed").type(BOOLEAN)
                                .description("휴무 여부"),
                            fieldWithPath("timePreferences").type(ARRAY)
                                .description("시간대별 무드 목록"),
                            fieldWithPath("preferenceGenres").type(ARRAY).description("선호/차단 장르"),
                            fieldWithPath("preferenceGenres[].genre").type(STRING)
                                .description("장르 +\n`BALLAD`, `HIPHOP`, `INDIE`, `ROCK`, `DANCE`, `CLASSIC`, `CHILDREN`"),
                            fieldWithPath("preferenceGenres[].preferenceType").type(STRING)
                                .description("선호 유형 +\n`LIKE` (선호), `DISLIKE` (차단)"),
                            fieldWithPath("photoUrls").type(ARRAY).description("매장 사진 URL 목록")
                        ),
                        responseFields(
                            fieldWithPath("orderId").type(STRING).description("주문 ID"),
                            fieldWithPath("amount").type(NUMBER).description("결제 금액 (원)"),
                            fieldWithPath("orderName").type(STRING).description("주문명"),
                            fieldWithPath("ownerAccount").type(OBJECT).description("오너 계정 정보"),
                            fieldWithPath("ownerAccount.loginId").type(STRING).description("로그인 ID"),
                            fieldWithPath("ownerAccount.password").type(STRING).description("비밀번호")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("GET /api/stores 실행 시")
    class FindStores {

        @Test
        @DisplayName("성공 : 매장 목록을 조회한다.")
        void findStores() throws Exception {
            // given
            FindStoresResponse response = new FindStoresResponse(List.of(), 0, 0);
            given(storeService.findStores(any())).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/stores"));

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
    @DisplayName("GET /api/stores/{storeId}/summary 실행 시")
    class FindStoreSummary {

        @Test
        @DisplayName("성공 : 매장 요약 정보를 조회한다.")
        void findStoreSummary() throws Exception {
            // given
            FindStoreSummaryResponse response = new FindStoreSummaryResponse(
                "카페 공명 홍대점", "카페", "서울 마포구 와우산로17길 11-8",
                List.of(new FindStoreSummaryResponse.BusinessHourItem(1, LocalTime.of(10, 0),
                    LocalTime.of(22, 0), false)),
                "ACTIVE", "PLAN_A", "photoUrl"
            );
            given(storeService.findStoreSummary(1L)).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(
                get("/api/stores/{storeId}/summary", 1L)
                    .requestAttr("owner", owner())
            );

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("storeId").description("매장 ID")
                        ),
                        responseFields(
                            fieldWithPath("name").type(STRING).description("매장명"),
                            fieldWithPath("industry").type(STRING).description("업종"),
                            fieldWithPath("address").type(STRING).description("주소"),
                            fieldWithPath("businessHours").type(ARRAY).description("영업시간 목록"),
                            fieldWithPath("businessHours[].dayOfWeek").type(NUMBER)
                                .description("요일 (1=월 ~ 7=일)"),
                            fieldWithPath("businessHours[].openTime").type(STRING)
                                .description("영업 시작 시간"),
                            fieldWithPath("businessHours[].closeTime").type(STRING)
                                .description("영업 종료 시간"),
                            fieldWithPath("businessHours[].isClosed").type(BOOLEAN)
                                .description("휴무 여부"),
                            fieldWithPath("subscriptionStatus").type(STRING)
                                .description("구독 상태 +\n`PENDING_PAYMENT`, `PENDING_ACTIVE`, `ACTIVE`, `EXPIRED`"),
                            fieldWithPath("planType").type(STRING)
                                .description("플랜 종류 +\n`PLAN_A`, `PLAN_B`"),
                            fieldWithPath("mainPhotoUrl").type(STRING)
                                .description("매장 대표 사진 url")
                        )
                    )
                );
        }
    }
}
