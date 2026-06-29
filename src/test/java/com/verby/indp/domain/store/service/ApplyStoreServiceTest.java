package com.verby.indp.domain.store.service;

import static com.verby.indp.fixture.OwnerFixture.owner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.service.OwnerService;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.store.MusicGenre;
import com.verby.indp.domain.store.PlayMethod;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.request.ApplyStoreRequest;
import com.verby.indp.domain.store.dto.request.BusinessHour;
import com.verby.indp.domain.store.dto.request.GenreItem;
import com.verby.indp.domain.store.dto.response.AddFirstSubscriptionResponse;
import com.verby.indp.domain.store.repository.StoreRepository;
import com.verby.indp.domain.store.vo.Genre;
import com.verby.indp.domain.store.vo.PlaylistType;
import com.verby.indp.domain.store.vo.Tempo;
import com.verby.indp.domain.store.vo.Vibe;
import com.verby.indp.domain.subscription.service.SubscriptionService;
import com.verby.indp.global.notification.ApplicationMailService;
import java.time.LocalTime;
import java.util.List;
import org.springframework.test.util.ReflectionTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApplyStoreServiceTest {

    @InjectMocks
    private ApplyStoreService applyStoreService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private OwnerService ownerService;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private ApplicationMailService applicationMailService;

    @Nested
    @DisplayName("applyStore 메서드 실행 시")
    class ApplyStore {

        @Test
        @DisplayName("실패 : 이미 존재하는 매장 이름이면 예외를 던진다.")
        void applyStoreWithDuplicateName() {
            // given
            given(storeRepository.existsByName("카페공명")).willReturn(true);

            ApplyStoreRequest request = new ApplyStoreRequest(
                "홍길동", "010-1234-5678", 1L, 3, "카페공명", "카페", "서울",
                List.of(new BusinessHour(1, LocalTime.of(10, 0), LocalTime.of(22, 0), false)),
                List.of(), "유튜브 뮤직", "인디", "20대",
                List.of(PlayMethod.Method.BLUETOOTH), List.of(Vibe.CALM), 3,
                PlaylistType.CONSISTENT_MOOD, List.of(), Tempo.CALM,
                List.of(new GenreItem(Genre.INDIE, MusicGenre.PreferenceType.LIKE)), "", "아늑한");

            // when
            Exception exception = catchException(() -> applyStoreService.applyStore(request));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("성공 : 매장 신청을 처리한다.")
        void applyStore() {
            // given
            ApplyStoreRequest request = new ApplyStoreRequest(
                "홍길동", "010-1234-5678", 1L, 3, "카페 공명", "카페", "서울",
                List.of(new BusinessHour(1, LocalTime.of(10, 0), LocalTime.of(22, 0), false)),
                List.of(), "유튜브 뮤직", "인디", "20대",
                List.of(PlayMethod.Method.BLUETOOTH), List.of(Vibe.CALM), 3,
                PlaylistType.CONSISTENT_MOOD, List.of(), Tempo.CALM, List.of(), "", "아늑한");

            given(storeRepository.existsByName("카페 공명")).willReturn(false);

            Owner owner = owner();
            given(ownerService.createOwner("홍길동", "카페 공명")).willReturn(owner);
            given(storeRepository.save(any())).willAnswer(inv -> {
                Store s = inv.getArgument(0);
                ReflectionTestUtils.setField(s, "storeId", 1L);
                return s;
            });

            given(subscriptionService.orderFirstSubscription(any(), any()))
                .willReturn(new AddFirstSubscriptionResponse("INDP-orderId", 3000, "orderName", null));

            // when
            AddFirstSubscriptionResponse result = applyStoreService.applyStore(request);

            // then
            assertThat(result).isNotNull();
        }
    }
}
