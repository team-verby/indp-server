package com.verby.indp.domain.recommendation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.policy.PricePolicyService;
import com.verby.indp.domain.policy.PricePolicy;
import com.verby.indp.domain.recommendation.SongRecommendation;
import com.verby.indp.domain.recommendation.dto.response.FindStoreRecommendationsResponse;
import com.verby.indp.domain.recommendation.repository.SongRecommendationRepository;
import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.playlist.service.PlaylistWebSocketService;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.fixture.PaymentFixture;
import com.verby.indp.fixture.PlanFixture;
import com.verby.indp.fixture.PlaylistFixture;
import com.verby.indp.fixture.PlaylistSongFixture;
import com.verby.indp.fixture.PricePolicyFixture;
import com.verby.indp.fixture.SongRecommendationFixture;
import com.verby.indp.fixture.StoreFixture;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private PlaylistWebSocketService playlistWebSocketService;

    @Nested
    @DisplayName("findRecommendedSongs 메서드 실행 시")
    class FindRecommendedSongs {

        @Test
        @DisplayName("성공 : 추천 노래 목록을 반환한다.")
        void findRecommendedSongs() {
            Store store = StoreFixture.store();
            given(storeService.getStoreById(1L)).willReturn(store);
            given(songRecommendationRepository.findAllByStoreAndStatus(store,
                SongRecommendation.RecommendationStatus.RECOMMENDED)).willReturn(List.of());

            FindStoreRecommendationsResponse result = songRecommendationService.findRecommendedSongs(
                1L);

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("orderSongRecommendation 메서드 실행 시")
    class OrderSongRecommendation {

        @Test
        @DisplayName("실패 : 구독이 활성화되지 않은 매장이면 예외를 던진다.")
        void orderSongRecommendationWithInactiveSubscription() {
            Store store = StoreFixture.inactiveStore();
            given(storeService.getStoreById(1L)).willReturn(store);

            Exception exception = catchException(() ->
                songRecommendationService.orderSongRecommendation(1L, "안녕 나의 사랑", "성시경",
                    "5zAEiu3SaO4", 259, "홍길동"));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 음악 추천 불가 플랜이면 예외를 던진다.")
        void orderSongRecommendationWithIneligiblePlan() {
            Plan planA = PlanFixture.planA();
            Store store = StoreFixture.storeWithActiveSubscriptionAndPlan(planA);
            given(storeService.getStoreById(1L)).willReturn(store);

            Exception exception = catchException(() ->
                songRecommendationService.orderSongRecommendation(1L, "안녕 나의 사랑", "성시경",
                    "5zAEiu3SaO4", 259, "홍길동"));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 현재 영업 중이 아닌 매장이면 예외를 던진다.")
        void orderSongRecommendationWithClosedStore() {
            Plan planB = PlanFixture.planB();
            Store store = StoreFixture.closedStoreWithActiveSubscriptionAndPlan(planB);
            given(storeService.getStoreById(1L)).willReturn(store);

            Exception exception = catchException(() ->
                songRecommendationService.orderSongRecommendation(1L, "안녕 나의 사랑", "성시경",
                    "5zAEiu3SaO4", 259, "홍길동"));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 플레이리스트가 없는 매장이면 예외를 던진다.")
        void orderSongRecommendationWithNoPlaylist() {
            Plan planB = PlanFixture.planB();
            Store store = StoreFixture.storeWithActiveSubscriptionAndPlan(planB);
            given(storeService.getStoreById(1L)).willReturn(store);

            Exception exception = catchException(() ->
                songRecommendationService.orderSongRecommendation(1L, "안녕 나의 사랑", "성시경",
                    "5zAEiu3SaO4", 259, "홍길동"));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("성공 : 음악 추천을 주문한다.")
        void orderSongRecommendation() {
            Plan planB = PlanFixture.planB();
            Store store = StoreFixture.storeWithActiveSubscriptionAndPlan(planB);
            store.assignPlaylist(PlaylistFixture.playlist());
            given(storeService.getStoreById(1L)).willReturn(store);

            PricePolicy pricePolicy = PricePolicyFixture.pricePolicy();
            given(pricePolicyService.getByPolicyKey("recommendation_fee")).willReturn(pricePolicy);
            given(songRecommendationRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

            Exception exception = catchException(() ->
                songRecommendationService.orderSongRecommendation(1L, "안녕 나의 사랑", "성시경",
                    "5zAEiu3SaO4", 259, "홍길동"));

            assertThat(exception).isNull();
        }
    }

    @Nested
    @DisplayName("confirmPayment 메서드 실행 시")
    class ConfirmPayment {

        @Test
        @DisplayName("성공 : 결제를 확인하고 추천곡을 플레이리스트에 추가한다.")
        void confirmPayment() {
            SongRecommendation recommendation = SongRecommendationFixture.songRecommendation();
            Store store = recommendation.getStore();
            Payment payment = PaymentFixture.payment();
            given(songRecommendationRepository.findByPayment(payment))
                .willReturn(Optional.of(recommendation));

            PlaylistSong playlistSong = PlaylistSongFixture.playlistSong();
            given(playlistService.addRecommendedSong(store, recommendation))
                .willReturn(playlistSong);
            willDoNothing().given(playlistWebSocketService)
                .sendSongRecommended(recommendation, playlistSong);

            Exception exception = catchException(
                () -> songRecommendationService.confirmPayment(payment));

            assertThat(exception).isNull();
            assertThat(recommendation.getStatus())
                .isEqualTo(SongRecommendation.RecommendationStatus.RECOMMENDED);
        }

        @Test
        @DisplayName("실패 : 결제에 대한 추천 정보가 없으면 예외를 던진다.")
        void confirmPaymentWithNotFound() {
            Payment payment = PaymentFixture.payment();
            given(songRecommendationRepository.findByPayment(payment)).willReturn(Optional.empty());

            Exception exception = catchException(
                () -> songRecommendationService.confirmPayment(payment));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }
}
