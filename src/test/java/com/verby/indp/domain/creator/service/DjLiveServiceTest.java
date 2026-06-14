package com.verby.indp.domain.creator.service;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.creator.repository.CreatorTrackRepository;
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

    @Nested
    @DisplayName("startLive 메서드 실행 시")
    class StartLive {

        @Test
        @DisplayName("성공 : 트랙이 있으면 라이브를 시작한다.")
        void startLive() {
            Creator creator = creatorWithId(1L);
            given(creatorTrackRepository.countByCreator(creator)).willReturn(1);

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
}
