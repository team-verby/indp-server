package com.verby.indp.domain.creator.service;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static com.verby.indp.fixture.CreatorTrackFixture.trackWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.common.exception.AuthException;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.CreatorTrack;
import com.verby.indp.domain.creator.dto.request.DjTrackUploadUrlRequest;
import com.verby.indp.domain.creator.dto.request.RegisterDjTrackRequest;
import com.verby.indp.domain.creator.dto.response.DjTrackResponse;
import com.verby.indp.domain.creator.dto.response.DjTrackUploadUrlResponse;
import com.verby.indp.domain.creator.dto.response.FindDjTracksResponse;
import com.verby.indp.domain.creator.repository.CreatorTrackRepository;
import com.verby.indp.global.image.ImageService;
import com.verby.indp.global.image.PresignedUpload;
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
class DjTrackServiceTest {

    @InjectMocks
    private DjTrackService djTrackService;

    @Mock
    private CreatorTrackRepository creatorTrackRepository;

    @Mock
    private ImageService imageService;

    @Nested
    @DisplayName("getTracks 메서드 실행 시")
    class GetTracks {

        @Test
        @DisplayName("성공 : 트랙 목록을 반환한다.")
        void getTracks() {
            Creator creator = creatorWithId(1L);
            CreatorTrack track = trackWithId(creator, 1L);
            given(creatorTrackRepository.findAllByCreatorOrderByCreatedAtAsc(creator))
                .willReturn(List.of(track));

            FindDjTracksResponse response = djTrackService.getTracks(creator);

            assertThat(response.tracks()).hasSize(1);
            assertThat(response.tracks().get(0).filename()).isEqualTo("morning_haze.mp3");
        }
    }

    @Nested
    @DisplayName("uploadTrack 메서드 실행 시")
    class UploadTrack {

        @Test
        @DisplayName("성공 : 트랙을 업로드한다.")
        void uploadTrack() {
            Creator creator = creatorWithId(1L);
            org.springframework.mock.web.MockMultipartFile file =
                new org.springframework.mock.web.MockMultipartFile(
                    "file", "track.mp3", "audio/mpeg", new byte[]{1, 2, 3});
            com.verby.indp.domain.creator.CreatorTrack saved =
                new com.verby.indp.domain.creator.CreatorTrack(
                    creator, "track.mp3", "https://cdn.example.com/track.mp3", "3:42", 222);
            org.springframework.test.util.ReflectionTestUtils.setField(saved, "trackId", 1L);

            given(imageService.uploadAudio(file)).willReturn("https://cdn.example.com/track.mp3");
            given(creatorTrackRepository.save(any())).willReturn(saved);

            com.verby.indp.domain.creator.dto.response.DjTrackResponse response =
                djTrackService.uploadTrack(creator, file, "3:42", 222);

            assertThat(response.filename()).isEqualTo("track.mp3");
        }
    }

    @Nested
    @DisplayName("createUploadUrl 메서드 실행 시")
    class CreateUploadUrl {

        @Test
        @DisplayName("성공 : presigned 업로드 URL과 스트리밍 URL을 반환한다.")
        void createUploadUrl() {
            given(imageService.createAudioUploadUrl(anyString()))
                .willReturn(new PresignedUpload(
                    "https://s3.example.com/audio/uuid-track.mp3?signature=x",
                    "https://cdn.example.com/audio/uuid-track.mp3"));

            DjTrackUploadUrlResponse response =
                djTrackService.createUploadUrl(new DjTrackUploadUrlRequest("track.mp3"));

            assertThat(response.uploadUrl()).contains("signature");
            assertThat(response.streamUrl()).isEqualTo("https://cdn.example.com/audio/uuid-track.mp3");
        }
    }

    @Nested
    @DisplayName("registerTrack 메서드 실행 시")
    class RegisterTrack {

        @Test
        @DisplayName("성공 : S3 직접 업로드된 트랙 메타데이터를 등록한다.")
        void registerTrack() {
            Creator creator = creatorWithId(1L);
            CreatorTrack saved = new CreatorTrack(
                creator, "track.mp3", "https://cdn.example.com/track.mp3", "3:42", 222);
            org.springframework.test.util.ReflectionTestUtils.setField(saved, "trackId", 1L);
            given(creatorTrackRepository.save(any())).willReturn(saved);

            DjTrackResponse response = djTrackService.registerTrack(creator,
                new RegisterDjTrackRequest("track.mp3", "https://cdn.example.com/track.mp3", "3:42", 222));

            assertThat(response.filename()).isEqualTo("track.mp3");
            assertThat(response.streamUrl()).isEqualTo("https://cdn.example.com/track.mp3");
        }
    }

    @Nested
    @DisplayName("deleteTrack 메서드 실행 시")
    class DeleteTrack {

        @Test
        @DisplayName("성공 : 트랙을 삭제한다.")
        void deleteTrack() {
            Creator creator = creatorWithId(1L);
            CreatorTrack track = trackWithId(creator, 1L);
            given(creatorTrackRepository.findById(1L)).willReturn(Optional.of(track));

            Exception exception = catchException(() -> djTrackService.deleteTrack(creator, 1L));

            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 트랙이면 예외를 던진다.")
        void deleteTrackNotFound() {
            Creator creator = creatorWithId(1L);
            given(creatorTrackRepository.findById(99L)).willReturn(Optional.empty());

            Exception exception = catchException(() -> djTrackService.deleteTrack(creator, 99L));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("실패 : 다른 크리에이터의 트랙이면 예외를 던진다.")
        void deleteTrackUnauthorized() {
            Creator creator1 = creatorWithId(1L);
            Creator creator2 = creatorWithId(2L);
            CreatorTrack track = trackWithId(creator2, 1L);
            given(creatorTrackRepository.findById(1L)).willReturn(Optional.of(track));

            Exception exception = catchException(() -> djTrackService.deleteTrack(creator1, 1L));

            assertThat(exception).isInstanceOf(AuthException.class);
        }
    }
}
