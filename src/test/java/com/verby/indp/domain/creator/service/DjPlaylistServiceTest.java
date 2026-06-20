package com.verby.indp.domain.creator.service;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.response.DjPlaylistDetailResponse;
import com.verby.indp.domain.creator.dto.response.FindDjPlaylistsResponse;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.creator.repository.CreatorTrackRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
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
class DjPlaylistServiceTest {

    @InjectMocks
    private DjPlaylistService djPlaylistService;

    @Mock
    private CreatorRepository creatorRepository;

    @Mock
    private CreatorTrackRepository creatorTrackRepository;

    @Mock
    private Clock clock;

    private void givenClock() {
        given(clock.instant()).willReturn(Instant.parse("2026-06-18T05:00:00Z"));
        given(clock.getZone()).willReturn(ZoneId.of("UTC"));
    }

    @Nested
    @DisplayName("getPlaylists 메서드 실행 시")
    class GetPlaylists {

        @Test
        @DisplayName("성공 : 활성 크리에이터 목록을 반환한다.")
        void getPlaylists() {
            Creator creator = creatorWithId(1L);
            givenClock();
            given(creatorRepository.findAll()).willReturn(List.of(creator));

            FindDjPlaylistsResponse response = djPlaylistService.getPlaylists();

            assertThat(response.playlists()).hasSize(1);
            assertThat(response.playlists().get(0).djName()).isEqualTo("DJ Parkwan");
        }
    }

    @Nested
    @DisplayName("getPlaylistDetail 메서드 실행 시")
    class GetPlaylistDetail {

        @Test
        @DisplayName("성공 : 채널 상세를 반환한다.")
        void getPlaylistDetail() {
            Creator creator = creatorWithId(1L);
            givenClock();
            given(creatorRepository.findById(1L)).willReturn(Optional.of(creator));
            given(creatorTrackRepository.findAllByCreatorOrderByCreatedAtAsc(creator))
                .willReturn(List.of());

            DjPlaylistDetailResponse response = djPlaylistService.getPlaylistDetail(1L);

            assertThat(response.djName()).isEqualTo("DJ Parkwan");
            assertThat(response.tracks()).isEmpty();
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 채널이면 예외를 던진다.")
        void getPlaylistDetailNotFound() {
            given(creatorRepository.findById(99L)).willReturn(Optional.empty());

            Exception exception = catchException(() -> djPlaylistService.getPlaylistDetail(99L));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }
}
