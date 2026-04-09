package com.verby.indp.domain.playlist.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.playlist.dto.response.FindStorePlaylistResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class PlaylistControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/stores/{storeId}/playlist 실행 시")
    class FindStorePlaylist {

        @Test
        @DisplayName("성공 : 매장 플레이리스트를 조회한다.")
        void findStorePlaylist() throws Exception {
            // given
            FindStorePlaylistResponse response = FindStorePlaylistResponse.from(List.of(), null);
            given(playlistService.getStorePlaylist(1L)).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(
                get("/api/stores/{storeId}/playlist", 1L));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("storeId").description("매장 ID")
                        ),
                        responseFields(
                            fieldWithPath("currentSong").type(JsonFieldType.NULL)
                                .description("현재 재생 중인 곡. 재생 중인 곡이 없으면 null"),
                            fieldWithPath("playlist").type(JsonFieldType.OBJECT).description("플레이리스트 정보"),
                            fieldWithPath("playlist.totalCount").type(NUMBER)
                                .description("전체 곡 수"),
                            fieldWithPath("playlist.recommendedCount").type(NUMBER)
                                .description("추천 곡 수"),
                            fieldWithPath("playlist.totalPlayTime").type(NUMBER)
                                .description("전체 재생 시간 (초)"),
                            fieldWithPath("playlist.songs").type(ARRAY).description("곡 목록")
                        )
                    )
                );
        }

        @Test
        @DisplayName("성공 : 현재 재생 중인 곡이 있는 플레이리스트를 조회한다.")
        void findStorePlaylistWithCurrentSong() throws Exception {
            // given
            com.verby.indp.domain.playlist.PlaylistSong song =
                org.mockito.Mockito.mock(com.verby.indp.domain.playlist.PlaylistSong.class);
            given(song.getPlaylistSongId()).willReturn(1L);
            given(song.getPlayOrder()).willReturn(10.0);
            given(song.getTitle()).willReturn("안녕 나의 사랑");
            given(song.getArtist()).willReturn("성시경");
            given(song.getPlayTime()).willReturn(259);
            given(song.isRecommended()).willReturn(false);

            com.verby.indp.domain.playlist.dto.response.CurrentSong currentSong =
                new com.verby.indp.domain.playlist.dto.response.CurrentSong(
                    1L, "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 42);

            FindStorePlaylistResponse response = FindStorePlaylistResponse.from(
                List.of(song), currentSong);
            given(playlistService.getStorePlaylist(1L)).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(
                get("/api/stores/{storeId}/playlist", 1L));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("storeId").description("매장 ID")
                        ),
                        responseFields(
                            fieldWithPath("currentSong").type(JsonFieldType.OBJECT)
                                .description("현재 재생 중인 곡. 재생 중인 곡이 없으면 null"),
                            fieldWithPath("currentSong.playlistSongId").type(NUMBER)
                                .description("플레이리스트 곡 ID"),
                            fieldWithPath("currentSong.title").type(STRING).description("곡 제목"),
                            fieldWithPath("currentSong.artist").type(STRING).description("아티스트"),
                            fieldWithPath("currentSong.elapsedSeconds").type(NUMBER)
                                .description("현재 곡 재생 경과 시간 (초)"),
                            fieldWithPath("playlist").type(JsonFieldType.OBJECT).description("플레이리스트 정보"),
                            fieldWithPath("playlist.totalCount").type(NUMBER)
                                .description("전체 곡 수"),
                            fieldWithPath("playlist.recommendedCount").type(NUMBER)
                                .description("추천 곡 수"),
                            fieldWithPath("playlist.totalPlayTime").type(NUMBER)
                                .description("전체 재생 시간 (초)"),
                            fieldWithPath("playlist.songs").type(ARRAY).description("곡 목록"),
                            fieldWithPath("playlist.songs[].playlistSongId").type(NUMBER)
                                .description("플레이리스트 곡 ID"),
                            fieldWithPath("playlist.songs[].playOrder").type(NUMBER)
                                .description("재생 순서"),
                            fieldWithPath("playlist.songs[].title").type(STRING).description("곡 제목"),
                            fieldWithPath("playlist.songs[].artist").type(STRING)
                                .description("아티스트"),
                            fieldWithPath("playlist.songs[].playTime").type(NUMBER)
                                .description("곡 길이 (초)"),
                            fieldWithPath("playlist.songs[].isRecommended").type(BOOLEAN)
                                .description("손님 추천 곡 여부"),
                            fieldWithPath("playlist.songs[].refereeName").type(JsonFieldType.NULL)
                                .description("추천한 손님 이름. 추천 곡이 아니면 null")
                        )
                    )
                );
        }
    }
}
