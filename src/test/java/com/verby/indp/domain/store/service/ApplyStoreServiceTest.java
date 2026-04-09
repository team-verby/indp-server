package com.verby.indp.domain.store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.service.OwnerService;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.store.PlayMethod;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.request.BusinessHour;
import com.verby.indp.domain.store.repository.StoreRepository;
import com.verby.indp.domain.store.dto.response.AddSubscriptionResponse;
import com.verby.indp.domain.store.vo.PlaylistType;
import com.verby.indp.domain.store.vo.Tempo;
import com.verby.indp.domain.store.vo.Vibe;
import com.verby.indp.domain.subscription.service.SubscriptionService;
import java.time.LocalTime;
import java.util.List;
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

    @Nested
    @DisplayName("applyStore 메서드 실행 시")
    class ApplyStore {

        @Test
        @DisplayName("실패 : 이미 존재하는 매장 이름이면 예외를 던진다.")
        void applyStoreWithDuplicateName() {
            // given
            given(storeRepository.existsByName("카페공명")).willReturn(true);

            com.verby.indp.domain.store.dto.request.ApplyStoreRequest request =
                org.mockito.Mockito.mock(
                    com.verby.indp.domain.store.dto.request.ApplyStoreRequest.class);
            given(request.name()).willReturn("카페공명");

            // when
            Exception exception = catchException(() -> applyStoreService.applyStore(request));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("성공 : 매장 신청을 처리한다.")
        void applyStore() {
            // given
            com.verby.indp.domain.store.dto.request.ApplyStoreRequest request =
                org.mockito.Mockito.mock(
                    com.verby.indp.domain.store.dto.request.ApplyStoreRequest.class);
            given(request.name()).willReturn("카페 공명");
            given(request.applicantName()).willReturn("홍길동");
            given(request.applicantPhone()).willReturn("010-1234-5678");
            given(request.platform()).willReturn("유튜브 뮤직");
            given(request.playedMusic()).willReturn("인디");
            given(request.rejectedSongNote()).willReturn("");
            given(request.playlistType()).willReturn(PlaylistType.CONSISTENT_MOOD);
            given(request.musicTempo()).willReturn(Tempo.CALM);
            given(request.mood()).willReturn("아늑한");
            given(request.playMethods()).willReturn(List.of(PlayMethod.Method.BLUETOOTH));
            given(request.timePreferences()).willReturn(List.of());
            given(request.preferenceGenres()).willReturn(List.of());
            BusinessHour bh = new BusinessHour(1, LocalTime.of(10, 0), LocalTime.of(22, 0), false);
            given(request.businessHours()).willReturn(List.of(bh));
            given(request.industry()).willReturn("카페");
            given(request.address()).willReturn("서울");
            given(request.customerAgeGroup()).willReturn("20대");
            given(request.lighting()).willReturn(3);
            given(request.vibes()).willReturn(List.of(Vibe.CALM));
            given(request.photoUrls()).willReturn(List.of());
            given(request.planId()).willReturn(1L);
            given(request.usagePeriod()).willReturn(3);

            given(storeRepository.existsByName("카페 공명")).willReturn(false);

            Owner owner = org.mockito.Mockito.mock(Owner.class);
            given(ownerService.createOwner("홍길동", "카페 공명")).willReturn(owner);
            given(storeRepository.save(any())).willAnswer(inv -> {
                Store s = inv.getArgument(0);
                ReflectionTestUtils.setField(s, "storeId", 1L);
                return s;
            });

            AddSubscriptionResponse subscriptionResponse = org.mockito.Mockito.mock(
                AddSubscriptionResponse.class);
            given(subscriptionService.orderSubscription(any(), anyLong(), any()))
                .willReturn(subscriptionResponse);

            // when
            AddSubscriptionResponse result = applyStoreService.applyStore(request);

            // then
            assertThat(result).isNotNull();
        }
    }
}
