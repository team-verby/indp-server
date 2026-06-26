package com.verby.indp.domain.creator.service;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.response.DjLiveListenersResponse;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.creator.repository.CreatorTrackRepository;
import com.verby.indp.domain.listening.repository.ListeningDailyRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DjLiveServiceTest {

    @InjectMocks
    private DjLiveService djLiveService;

    @Mock
    private CreatorRepository creatorRepository;

    @Mock
    private CreatorTrackRepository creatorTrackRepository;

    @Mock
    private ListeningDailyRepository listeningDailyRepository;

    @Mock
    private Clock clock;

    @Nested
    @DisplayName("startLive 메서드 실행 시")
    class StartLive {

        @Test
        @DisplayName("성공 : 트랙이 있으면 라이브를 시작한다.")
        void startLive() {
            Creator creator = creatorWithId(1L);
            given(creatorTrackRepository.countByCreator(creator)).willReturn(1);
            given(clock.instant()).willReturn(Instant.parse("2026-06-18T05:00:00Z"));
            given(clock.getZone()).willReturn(ZoneId.of("UTC"));

            Exception exception = catchException(() -> djLiveService.startLive(creator));

            assertThat(exception).isNull();
            assertThat(creator.isLive()).isTrue();
        }

        @Test
        @DisplayName("실패 : 트랙이 없으면 예외를 던진다.")
        void startLiveWithNoTracks() {
            Creator creator = creatorWithId(1L);
            given(creatorTrackRepository.countByCreator(creator)).willReturn(0);

            Exception exception = catchException(() -> djLiveService.startLive(creator));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("stopLive 메서드 실행 시")
    class StopLive {

        @Test
        @DisplayName("성공 : 라이브를 종료한다.")
        void stopLive() {
            Creator creator = creatorWithId(1L);
            creator.startLive();

            Exception exception = catchException(() -> djLiveService.stopLive(creator));

            assertThat(exception).isNull();
            assertThat(creator.isLive()).isFalse();
        }
    }

    @Nested
    @DisplayName("heartbeat 메서드 실행 시")
    class Heartbeat {

        @Test
        @DisplayName("성공 : 라이브 중인 채널이 TTL 윈도우 내 라이브로 판정된다.")
        void heartbeat() {
            Creator creator = creatorWithId(1L);
            creator.startLive();
            Instant now = Instant.parse("2026-06-18T05:00:00Z");
            given(clock.instant()).willReturn(now);
            given(clock.getZone()).willReturn(ZoneId.of("UTC"));

            djLiveService.heartbeat(creator);

            LocalDateTime asOf = LocalDateTime.ofInstant(now, ZoneId.of("UTC"));
            assertThat(creator.isLiveWithin(asOf, DjLiveService.LIVE_TTL_SEC)).isTrue();
            assertThat(creator.isLiveWithin(
                asOf.plusSeconds(DjLiveService.LIVE_TTL_SEC + 1), DjLiveService.LIVE_TTL_SEC))
                .isFalse();
        }
    }

    @Nested
    @DisplayName("getListeners 메서드 실행 시")
    class GetListeners {

        @Test
        @DisplayName("성공 : 최근 윈도우 내 청취 중인 사용자 수를 반환한다.")
        void getListeners() {
            Creator creator = creatorWithId(1L);
            given(clock.instant()).willReturn(Instant.parse("2026-06-18T05:00:00Z"));
            given(clock.getZone()).willReturn(ZoneId.of("UTC"));
            given(listeningDailyRepository.countByCreatorIdAndYmdAndUpdatedAtGreaterThanEqual(
                eq(1L), eq(LocalDate.of(2026, 6, 18)), any(LocalDateTime.class)))
                .willReturn(3);

            DjLiveListenersResponse response = djLiveService.getListeners(creator);

            assertThat(response.count()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("syncAutoLive 메서드 실행 시")
    class SyncAutoLive {

        // 2026-06-18T05:00:00Z == UTC 05:00 (라이브 창 08~23시 밖)
        private void givenClockUtc(String instant) {
            given(clock.instant()).willReturn(Instant.parse(instant));
            given(clock.getZone()).willReturn(ZoneId.of("UTC"));
        }

        @Test
        @DisplayName("성공 : 라이브 창 안 + 트랙이 있으면 라이브를 켠다.")
        void turnsOnWithinWindow() {
            Creator creator = creatorWithId(1L);
            creator.enableAutoLive(LocalTime.of(8, 0), LocalTime.of(23, 0));
            givenClockUtc("2026-06-18T12:00:00Z"); // UTC 12:00 → 창 안
            given(creatorRepository.findAllByAutoLiveTrueAndActiveTrue()).willReturn(List.of(creator));
            given(creatorTrackRepository.countByCreator(creator)).willReturn(5);

            djLiveService.syncAutoLive();

            LocalDateTime asOf = LocalDateTime.of(2026, 6, 18, 12, 0);
            assertThat(creator.isLive()).isTrue();
            assertThat(creator.isLiveWithin(asOf, DjLiveService.LIVE_TTL_SEC)).isTrue();
        }

        @Test
        @DisplayName("성공 : 라이브 창 안이어도 트랙이 없으면 라이브로 켜지 않는다.")
        void skipsWhenNoTracks() {
            Creator creator = creatorWithId(1L);
            creator.enableAutoLive(LocalTime.of(8, 0), LocalTime.of(23, 0));
            givenClockUtc("2026-06-18T12:00:00Z");
            given(creatorRepository.findAllByAutoLiveTrueAndActiveTrue()).willReturn(List.of(creator));
            given(creatorTrackRepository.countByCreator(creator)).willReturn(0);

            djLiveService.syncAutoLive();

            assertThat(creator.isLive()).isFalse();
        }

        @Test
        @DisplayName("성공 : 라이브 창 밖 + 라이브 중이면 라이브를 끈다.")
        void turnsOffOutsideWindow() {
            Creator creator = creatorWithId(1L);
            creator.enableAutoLive(LocalTime.of(8, 0), LocalTime.of(23, 0));
            creator.startLive();
            givenClockUtc("2026-06-18T05:00:00Z"); // UTC 05:00 → 창 밖
            given(creatorRepository.findAllByAutoLiveTrueAndActiveTrue()).willReturn(List.of(creator));

            djLiveService.syncAutoLive();

            assertThat(creator.isLive()).isFalse();
        }

        @Test
        @DisplayName("성공 : 24시간(창 null) 계정은 언제나 라이브를 켠다.")
        void alwaysLive() {
            Creator creator = creatorWithId(1L);
            creator.enableAutoLive(null, null);
            givenClockUtc("2026-06-18T05:00:00Z");
            given(creatorRepository.findAllByAutoLiveTrueAndActiveTrue()).willReturn(List.of(creator));
            given(creatorTrackRepository.countByCreator(creator)).willReturn(3);

            djLiveService.syncAutoLive();

            assertThat(creator.isLive()).isTrue();
        }
    }
}
