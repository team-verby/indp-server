package com.verby.indp.domain.playlist.service;

import static com.verby.indp.fixture.MusicCatalogSongFixture.musicCatalogSong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.verby.indp.domain.playlist.MusicCatalogSong;
import com.verby.indp.domain.playlist.dto.request.UpdateMusicCatalogRequest;
import com.verby.indp.domain.playlist.dto.request.UpdateMusicCatalogRequest.MoodCatalog;
import com.verby.indp.domain.playlist.dto.request.UpdateMusicCatalogRequest.SongItem;
import com.verby.indp.domain.playlist.dto.response.FindMusicCatalogResponse;
import com.verby.indp.domain.playlist.repository.MusicCatalogSongRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminMusicCatalogServiceTest {

    @InjectMocks
    private AdminMusicCatalogService adminMusicCatalogService;

    @Mock
    private MusicCatalogSongRepository musicCatalogSongRepository;

    @Captor
    private ArgumentCaptor<List<MusicCatalogSong>> songsCaptor;

    @Nested
    @DisplayName("findMusicCatalog 메서드 실행 시")
    class FindMusicCatalog {

        @Test
        @DisplayName("성공 : 무드별로 그룹핑된 음원 카탈로그를 반환한다.")
        void findMusicCatalog() {
            // given
            given(musicCatalogSongRepository.findAllByOrderByMoodAscPositionAsc())
                .willReturn(List.of(musicCatalogSong()));

            // when
            FindMusicCatalogResponse response = adminMusicCatalogService.findMusicCatalog();

            // then
            assertThat(response.moods()).hasSize(1);
            assertThat(response.moods().get(0).mood()).isEqualTo("잔잔한");
            assertThat(response.moods().get(0).songs()).hasSize(1);
            assertThat(response.moods().get(0).songs().get(0).title()).isEqualTo("안녕 나의 사랑");
        }
    }

    @Nested
    @DisplayName("updateMusicCatalog 메서드 실행 시")
    class UpdateMusicCatalog {

        @Test
        @DisplayName("성공 : 기존 카탈로그를 전체 삭제 후 새 곡들을 저장한다.")
        void updateMusicCatalog() {
            // given
            UpdateMusicCatalogRequest request = new UpdateMusicCatalogRequest(List.of(
                new MoodCatalog("잔잔한", List.of(
                    new SongItem("안녕 나의 사랑", "성시경", "4:19", "5zAEiu3SaO4"),
                    new SongItem("그때 그 순간 그대로", "위너", "3:33", "abcdefghijk")
                ))
            ));

            // when
            adminMusicCatalogService.updateMusicCatalog(request);

            // then
            then(musicCatalogSongRepository).should().deleteAllInBatch();
            then(musicCatalogSongRepository).should().saveAll(songsCaptor.capture());
            List<MusicCatalogSong> saved = songsCaptor.getValue();
            assertThat(saved).hasSize(2);
            assertThat(saved.get(0).getPosition()).isEqualTo(1);
            assertThat(saved.get(1).getPosition()).isEqualTo(2);
            assertThat(saved.get(0).getMood()).isEqualTo("잔잔한");
        }

        @Test
        @DisplayName("성공 : title이 비어있는 곡은 건너뛴다.")
        void updateMusicCatalogSkipsBlankTitle() {
            // given
            UpdateMusicCatalogRequest request = new UpdateMusicCatalogRequest(List.of(
                new MoodCatalog("잔잔한", List.of(
                    new SongItem("안녕 나의 사랑", "성시경", "4:19", "5zAEiu3SaO4"),
                    new SongItem(" ", "익명", "", "")
                ))
            ));

            // when
            adminMusicCatalogService.updateMusicCatalog(request);

            // then
            then(musicCatalogSongRepository).should().saveAll(songsCaptor.capture());
            assertThat(songsCaptor.getValue()).hasSize(1);
        }

        @Test
        @DisplayName("성공 : moods가 null이면 전체 삭제만 수행한다.")
        void updateMusicCatalogWithNullMoods() {
            // given
            UpdateMusicCatalogRequest request = new UpdateMusicCatalogRequest(null);

            // when
            adminMusicCatalogService.updateMusicCatalog(request);

            // then
            then(musicCatalogSongRepository).should().deleteAllInBatch();
            then(musicCatalogSongRepository).should(org.mockito.Mockito.never()).saveAll(anyList());
        }
    }
}
