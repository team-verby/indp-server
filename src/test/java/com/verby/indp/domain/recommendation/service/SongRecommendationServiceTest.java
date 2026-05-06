package com.verby.indp.domain.recommendation.service;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.playlist.service.PlaylistSseService;
import com.verby.indp.domain.policy.PricePolicy;
import com.verby.indp.domain.policy.PricePolicyService;
import com.verby.indp.domain.recommendation.SongRecommendation;
import com.verby.indp.domain.recommendation.dto.response.FindStoreRecommendationsResponse;
import com.verby.indp.domain.recommendation.repository.SongRecommendationRepository;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.fixture.*;
import com.verby.indp.global.slack.SlackNotificationService;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.lenient;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class SongRecommendationServiceTest {

    @InjectMocks
    private SongRecommendationService songRecommendationService;

    @Mock
    private SongRecommendationRepository songRecommendationRepository;

    @Mock
    private StoreService storeService;

    @Mock
    private PricePolicyService pricePolicyService;

    @Mock
    private PlaylistService playlistService;

    @Mock
    private PlaylistSseService playlistSseService;

    @Mock
    private SlackNotificationService slackNotificationService;

    @Mock
    private Clock clock;

    @BeforeEach
    void setUp() {
        lenient().when(clock.instant()).thenReturn(Instant.parse("2026-04-24T03:00:00Z"));
        lenient().when(clock.getZone()).thenReturn(ZoneId.systemDefault());
    }

    @Nested
    @DisplayName("findRecommendedSongs 메서드 실행 시")
    class FindRecommendedSongs {

        @Test
        @DisplayName("성공 : 추천 노래 목록을 반환한다.")
        void findRecommendedSongs() {
            // given
            Store store = StoreFixture.store();
            given(storeService.getStoreById(1L)).willReturn(store);
            given(songRecommendationRepository.findAllByStoreAndStatus(store,
                SongRecommendation.RecommendationStatus.RECOMMENDED)).willReturn(List.of());

            // when
            FindStoreRecommendationsResponse result = songRecommendationService.findRecommendedSongs(
                1L);

            // then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("orderSongRecommendation 메서드 실행 시")
    class OrderSongRecommendation {

        @Test
        @DisplayName("실패 : 구독이 활성화되지 않은 매장이면 예외를 던진다.")
        void orderSongRecommendationWithInactiveSubscription() {
            // given
            Store store = StoreFixture.inactiveStore();
            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            Exception exception = catchException(() ->
                songRecommendationService.orderSongRecommendation(1L, "안녕 나의 사랑", "성시경",
                    "5zAEiu3SaO4", 259, "홍길동"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 음악 추천 불가 플랜이면 예외를 던진다.")
        void orderSongRecommendationWithIneligiblePlan() {
            // given
            Plan planA = PlanFixture.planA();
            Store store = StoreFixture.storeWithActiveSubscriptionAndPlan(planA);
            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            Exception exception = catchException(() ->
                songRecommendationService.orderSongRecommendation(1L, "안녕 나의 사랑", "성시경",
                    "5zAEiu3SaO4", 259, "홍길동"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 현재 영업 중이 아닌 매장이면 예외를 던진다.")
        void orderSongRecommendationWithClosedStore() {
            // given
            Plan planB = PlanFixture.planB();
            Store store = StoreFixture.closedStoreWithActiveSubscriptionAndPlan(planB);
            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            Exception exception = catchException(() ->
                songRecommendationService.orderSongRecommendation(1L, "안녕 나의 사랑", "성시경",
                    "5zAEiu3SaO4", 259, "홍길동"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 플레이리스트가 없는 매장이면 예외를 던진다.")
        void orderSongRecommendationWithNoPlaylist() {
            // given
            Plan planB = PlanFixture.planB();
            Store store = StoreFixture.storeWithActiveSubscriptionAndPlan(planB);
            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            Exception exception = catchException(() ->
                songRecommendationService.orderSongRecommendation(1L, "안녕 나의 사랑", "성시경",
                    "5zAEiu3SaO4", 259, "홍길동"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("성공 : 자정을 넘기는 영업시간(당일)에 영업 중인 매장에서 플레이리스트가 없으면 예외를 던진다.")
        void orderSongRecommendationWithPastMidnightCurrentDay() {
            // given
            Plan planB = PlanFixture.planB();
            Store store = StoreFixture.pastMidnightCurrentDayStoreWithPlan(planB);
            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            Exception exception = catchException(() ->
                songRecommendationService.orderSongRecommendation(1L, "안녕 나의 사랑", "성시경",
                    "5zAEiu3SaO4", 259, "홍길동"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("성공 : 전날 자정을 넘긴 영업시간에 영업 중인 매장에서 플레이리스트가 없으면 예외를 던진다.")
        void orderSongRecommendationWithPastMidnightPrevDay() {
            // given
            Plan planB = PlanFixture.planB();
            Store store = StoreFixture.pastMidnightPrevDayStoreWithPlan(planB);
            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            Exception exception = catchException(() ->
                songRecommendationService.orderSongRecommendation(1L, "안녕 나의 사랑", "성시경",
                    "5zAEiu3SaO4", 259, "홍길동"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("성공 : 음악 추천을 주문한다.")
        void orderSongRecommendation() {
            // given
            Plan planB = PlanFixture.planB();
            Store store = StoreFixture.storeWithActiveSubscriptionAndPlan(planB);
            store.assignPlaylist(PlaylistFixture.playlist());
            given(storeService.getStoreById(1L)).willReturn(store);

            PricePolicy pricePolicy = PricePolicyFixture.pricePolicy();
            given(pricePolicyService.getByPolicyKey("recommendation_fee")).willReturn(pricePolicy);
            given(songRecommendationRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

            // when
            Exception exception = catchException(() ->
                songRecommendationService.orderSongRecommendation(1L, "안녕 나의 사랑", "성시경",
                    "5zAEiu3SaO4", 259, "홍길동"));

            // then
            assertThat(exception).isNull();
        }
    }

    @Nested
    @DisplayName("confirmPayment 메서드 실행 시")
    class ConfirmPayment {

        @Test
        @DisplayName("성공 : 결제를 확인하고 추천곡을 플레이리스트에 추가한다.")
        void confirmPayment() {
            // given
            SongRecommendation recommendation = SongRecommendationFixture.songRecommendation();
            Store store = recommendation.getStore();
            Payment payment = PaymentFixture.payment();
            given(songRecommendationRepository.findByPayment(payment))
                .willReturn(Optional.of(recommendation));

            PlaylistSong playlistSong = PlaylistSongFixture.playlistSong();
            given(playlistService.addRecommendedSong(store, recommendation))
                .willReturn(playlistSong);
            willDoNothing().given(playlistSseService)
                .sendSongRecommended(recommendation, playlistSong);

            // when
            Exception exception = catchException(
                () -> songRecommendationService.confirmPayment(payment));

            // then
            assertThat(exception).isNull();
            assertThat(recommendation.getStatus())
                .isEqualTo(SongRecommendation.RecommendationStatus.RECOMMENDED);
        }

        @Test
        @DisplayName("실패 : 결제에 대한 추천 정보가 없으면 예외를 던진다.")
        void confirmPaymentWithNotFound() {
            // given
            Payment payment = PaymentFixture.payment();
            given(songRecommendationRepository.findByPayment(payment)).willReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> songRecommendationService.confirmPayment(payment));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }
}
