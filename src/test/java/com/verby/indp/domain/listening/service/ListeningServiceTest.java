package com.verby.indp.domain.listening.service;

import static com.verby.indp.fixture.UserFixture.userWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.listening.ListeningDaily;
import com.verby.indp.domain.listening.SettlementPolicy;
import com.verby.indp.domain.listening.dto.request.HeartbeatRequest;
import com.verby.indp.domain.listening.repository.ListeningDailyRepository;
import com.verby.indp.domain.subscription.UserSubscriptionStatus;
import com.verby.indp.domain.subscription.repository.UserSubscriptionRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListeningServiceTest {

    @InjectMocks
    private ListeningService listeningService;

    @Mock
    private ListeningDailyRepository listeningDailyRepository;

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @Mock
    private CreatorRepository creatorRepository;

    @Mock
    private Clock clock;

    private static final LocalDate TODAY = LocalDate.of(2026, 6, 18);

    private void fixToday() {
        given(clock.instant()).willReturn(Instant.parse("2026-06-18T00:00:00Z"));
        given(clock.getZone()).willReturn(ZoneId.of("UTC"));
    }

    private void givenActiveSubscriber(User user) {
        given(userSubscriptionRepository
            .existsByUserAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                eq(user), eq(UserSubscriptionStatus.ACTIVE), any(), any()))
            .willReturn(true);
    }

    @Nested
    @DisplayName("heartbeat 메서드 실행 시")
    class Heartbeat {

        @Test
        @DisplayName("성공 : 신규 청취는 listening_daily를 생성하고 델타를 저장한다.")
        void createsNewDaily() {
            User user = userWithId(1L);
            fixToday();
            givenActiveSubscriber(user);
            given(creatorRepository.existsByCreatorIdAndActiveTrue(10L)).willReturn(true);
            given(listeningDailyRepository.findByUserIdAndCreatorIdAndYmd(1L, 10L, TODAY))
                .willReturn(Optional.empty());

            listeningService.heartbeat(user, new HeartbeatRequest(10L, 300));

            ArgumentCaptor<ListeningDaily> captor = ArgumentCaptor.forClass(ListeningDaily.class);
            verify(listeningDailyRepository).save(captor.capture());
            ListeningDaily saved = captor.getValue();
            assertThat(saved.getUserId()).isEqualTo(1L);
            assertThat(saved.getCreatorId()).isEqualTo(10L);
            assertThat(saved.getYmd()).isEqualTo(TODAY);
            assertThat(saved.getSeconds()).isEqualTo(300);
        }

        @Test
        @DisplayName("성공 : 기존 청취 기록이 있으면 델타를 누적한다.")
        void accumulatesExisting() {
            User user = userWithId(1L);
            fixToday();
            givenActiveSubscriber(user);
            given(creatorRepository.existsByCreatorIdAndActiveTrue(10L)).willReturn(true);
            ListeningDaily existing = new ListeningDaily(1L, 10L, TODAY, 100);
            given(listeningDailyRepository.findByUserIdAndCreatorIdAndYmd(1L, 10L, TODAY))
                .willReturn(Optional.of(existing));

            listeningService.heartbeat(user, new HeartbeatRequest(10L, 200));

            assertThat(existing.getSeconds()).isEqualTo(300);
            verify(listeningDailyRepository, never()).save(any());
        }

        @Test
        @DisplayName("성공 : 비트당 상한(MAX_BEAT_SEC)을 초과하는 델타는 클램프한다.")
        void clampsOversizedBeat() {
            User user = userWithId(1L);
            fixToday();
            givenActiveSubscriber(user);
            given(creatorRepository.existsByCreatorIdAndActiveTrue(10L)).willReturn(true);
            given(listeningDailyRepository.findByUserIdAndCreatorIdAndYmd(1L, 10L, TODAY))
                .willReturn(Optional.empty());

            listeningService.heartbeat(user, new HeartbeatRequest(10L, 99999));

            ArgumentCaptor<ListeningDaily> captor = ArgumentCaptor.forClass(ListeningDaily.class);
            verify(listeningDailyRepository).save(captor.capture());
            assertThat(captor.getValue().getSeconds()).isEqualTo(SettlementPolicy.MAX_BEAT_SEC);
        }

        @Test
        @DisplayName("무시 : 활성 구독이 없으면 기록하지 않는다.")
        void ignoresNonSubscriber() {
            User user = userWithId(1L);
            fixToday();
            given(userSubscriptionRepository
                .existsByUserAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                    eq(user), eq(UserSubscriptionStatus.ACTIVE), any(), any()))
                .willReturn(false);

            listeningService.heartbeat(user, new HeartbeatRequest(10L, 300));

            verify(listeningDailyRepository, never()).save(any());
            verify(listeningDailyRepository, never())
                .findByUserIdAndCreatorIdAndYmd(any(), any(), any());
        }

        @Test
        @DisplayName("무시 : 존재하지 않거나 비활성 크리에이터는 기록하지 않는다.")
        void ignoresUnknownCreator() {
            User user = userWithId(1L);
            fixToday();
            givenActiveSubscriber(user);
            given(creatorRepository.existsByCreatorIdAndActiveTrue(999L)).willReturn(false);

            listeningService.heartbeat(user, new HeartbeatRequest(999L, 300));

            verify(listeningDailyRepository, never()).save(any());
            verify(listeningDailyRepository, never())
                .findByUserIdAndCreatorIdAndYmd(any(), any(), any());
        }

        @Test
        @DisplayName("무시 : 0 이하 델타는 검증을 거치지 않고 즉시 무시한다.")
        void ignoresNonPositiveDelta() {
            User user = userWithId(1L);

            listeningService.heartbeat(user, new HeartbeatRequest(10L, 0));

            verify(userSubscriptionRepository, never())
                .existsByUserAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                    any(), any(), any(), any());
            verify(listeningDailyRepository, never()).save(any());
        }
    }
}
