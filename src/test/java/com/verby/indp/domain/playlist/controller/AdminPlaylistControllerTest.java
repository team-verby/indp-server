package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.playlist.dto.request.SchedulePlaylistsUpdateRequest;
import com.verby.indp.domain.playlist.dto.request.SchedulePlaylistsUpdateRequest.SchedulePlaylistItem;
import com.verby.indp.domain.playlist.dto.request.SchedulePlaylistsUpdateRequest.SchedulePlaylistItem.SongItem;
import com.verby.indp.fixture.AdminFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminPlaylistControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("POST /api/admin/playlists/schedule 실행 시")
    class SchedulePlaylistUpdates {

        @Test
        @DisplayName("성공 : 플레이리스트 업데이트를 예약한다.")
        void schedulePlaylistUpdates() throws Exception {
            // given
            Admin admin = AdminFixture.admin();
            givenAdminAuth(admin);
            willDoNothing().given(adminPlaylistService).addScheduledPlaylists(any());

            SchedulePlaylistsUpdateRequest request = new SchedulePlaylistsUpdateRequest(
                List.of(new SchedulePlaylistItem(
                    "카페 공명 홍대점",
                    List.of(new SongItem("안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259)),
                    LocalDateTime.of(2026, 4, 10, 9, 0)
                ))
            );

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/admin/playlists/schedule")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isNoContent())
                .andDo(
                    restDocs.document(
                        requestFields(
                            fieldWithPath("schedulePlaylists").type(ARRAY)
                                .description("플레이리스트 업데이트 예약 목록"),
                            fieldWithPath("schedulePlaylists[].storeName").type(STRING)
                                .description("매장명"),
                            fieldWithPath("schedulePlaylists[].scheduledAt").type(STRING)
                                .description("업데이트 예약 시각"),
                            fieldWithPath("schedulePlaylists[].songs").type(ARRAY)
                                .description("업데이트할 곡 목록"),
                            fieldWithPath("schedulePlaylists[].songs[].title").type(STRING)
                                .description("곡 제목"),
                            fieldWithPath("schedulePlaylists[].songs[].artist").type(STRING)
                                .description("아티스트"),
                            fieldWithPath("schedulePlaylists[].songs[].vid").type(STRING)
                                .description("YouTube 영상 ID"),
                            fieldWithPath("schedulePlaylists[].songs[].playTime").type(NUMBER)
                                .description("곡 길이 (초)")
                        )
                    )
                );
        }
    }
}
