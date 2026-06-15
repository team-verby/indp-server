package com.verby.indp.domain.playlist.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.playlist.dto.request.CreateFixedPlaylistSongRequest;
import com.verby.indp.domain.playlist.dto.response.FindFixedPlaylistSongsResponse;
import com.verby.indp.domain.playlist.dto.response.FindFixedPlaylistSongsResponse.FixedPlaylistSongItem;
import com.verby.indp.fixture.AdminFixture;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class AdminFixedPlaylistSongControllerTest extends BaseControllerTest {

    private Admin admin() {
        return AdminFixture.admin();
    }

    private FixedPlaylistSongItem item() {
        return new FixedPlaylistSongItem(1L, "카페 공명 홍대점",
            LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30), 14, 3,
            "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259);
    }

    @Nested
    @DisplayName("POST /api/admin/fixed-songs 실행 시")
    class AddFixedPlaylistSong {

        @Test
        @DisplayName("성공 : 특정곡(고정곡)을 저장한다.")
        void addFixedPlaylistSong() throws Exception {
            // given
            givenAdminAuth(admin());
            willDoNothing().given(fixedPlaylistSongService).addFixedPlaylistSong(any());

            CreateFixedPlaylistSongRequest request = new CreateFixedPlaylistSongRequest(
                "카페 공명 홍대점", LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30),
                14, 3, "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259);

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/admin/fixed-songs")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                    requestFields(
                        fieldWithPath("storeName").type(STRING).description("매장명"),
                        fieldWithPath("startDate").type(STRING).description("적용 시작일"),
                        fieldWithPath("endDate").type(STRING).description("적용 종료일"),
                        fieldWithPath("hour").type(NUMBER)
                            .description("시간대(0~23). 엑셀 모드는 null").optional(),
                        fieldWithPath("position").type(NUMBER).description("삽입 순서"),
                        fieldWithPath("title").type(STRING).description("곡 제목"),
                        fieldWithPath("artist").type(STRING).description("아티스트"),
                        fieldWithPath("vid").type(STRING).description("YouTube 영상 ID"),
                        fieldWithPath("playTime").type(NUMBER).description("곡 길이 (초)")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("GET /api/admin/fixed-songs 실행 시")
    class FindFixedPlaylistSongs {

        @Test
        @DisplayName("성공 : 저장된 특정곡 목록을 조회한다.")
        void findFixedPlaylistSongs() throws Exception {
            // given
            givenAdminAuth(admin());
            given(fixedPlaylistSongService.findFixedPlaylistSongs())
                .willReturn(new FindFixedPlaylistSongsResponse(List.of(item())));

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/admin/fixed-songs")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("fixedSongs").type(ARRAY).description("특정곡 목록"),
                        fieldWithPath("fixedSongs[].id").type(NUMBER).description("특정곡 ID"),
                        fieldWithPath("fixedSongs[].storeName").type(STRING).description("매장명"),
                        fieldWithPath("fixedSongs[].startDate").type(STRING).description("적용 시작일"),
                        fieldWithPath("fixedSongs[].endDate").type(STRING).description("적용 종료일"),
                        fieldWithPath("fixedSongs[].hour").type(NUMBER)
                            .description("시간대(0~23). 엑셀 모드는 null").optional(),
                        fieldWithPath("fixedSongs[].position").type(NUMBER).description("삽입 순서"),
                        fieldWithPath("fixedSongs[].title").type(STRING).description("곡 제목"),
                        fieldWithPath("fixedSongs[].artist").type(STRING).description("아티스트"),
                        fieldWithPath("fixedSongs[].vid").type(STRING).description("YouTube 영상 ID"),
                        fieldWithPath("fixedSongs[].playTime").type(NUMBER).description("곡 길이 (초)")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("GET /api/admin/fixed-songs/active 실행 시")
    class FindActiveFixedPlaylistSongs {

        @Test
        @DisplayName("성공 : 특정 날짜에 적용되는 특정곡 목록을 조회한다.")
        void findActiveFixedPlaylistSongs() throws Exception {
            // given
            givenAdminAuth(admin());
            given(fixedPlaylistSongService.findActiveFixedPlaylistSongs(any()))
                .willReturn(new FindFixedPlaylistSongsResponse(List.of(item())));

            // when
            ResultActions resultActions = mockMvc.perform(
                get("/api/admin/fixed-songs/active")
                    .param("date", "2026-06-15")
                    .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    queryParameters(
                        parameterWithName("date").description("조회 기준 날짜 (YYYY-MM-DD)")
                    ),
                    responseFields(
                        fieldWithPath("fixedSongs").type(ARRAY).description("특정곡 목록"),
                        fieldWithPath("fixedSongs[].id").type(NUMBER).description("특정곡 ID"),
                        fieldWithPath("fixedSongs[].storeName").type(STRING).description("매장명"),
                        fieldWithPath("fixedSongs[].startDate").type(STRING).description("적용 시작일"),
                        fieldWithPath("fixedSongs[].endDate").type(STRING).description("적용 종료일"),
                        fieldWithPath("fixedSongs[].hour").type(NUMBER)
                            .description("시간대(0~23). 엑셀 모드는 null").optional(),
                        fieldWithPath("fixedSongs[].position").type(NUMBER).description("삽입 순서"),
                        fieldWithPath("fixedSongs[].title").type(STRING).description("곡 제목"),
                        fieldWithPath("fixedSongs[].artist").type(STRING).description("아티스트"),
                        fieldWithPath("fixedSongs[].vid").type(STRING).description("YouTube 영상 ID"),
                        fieldWithPath("fixedSongs[].playTime").type(NUMBER).description("곡 길이 (초)")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("DELETE /api/admin/fixed-songs/{fixedPlaylistSongId} 실행 시")
    class DeleteFixedPlaylistSong {

        @Test
        @DisplayName("성공 : 특정곡을 삭제한다.")
        void deleteFixedPlaylistSong() throws Exception {
            // given
            givenAdminAuth(admin());
            willDoNothing().given(fixedPlaylistSongService).deleteFixedPlaylistSong(eq(1L));

            // when
            ResultActions resultActions = mockMvc.perform(
                delete("/api/admin/fixed-songs/{fixedPlaylistSongId}", 1L)
                    .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                    pathParameters(
                        parameterWithName("fixedPlaylistSongId").description("삭제할 특정곡 ID")
                    )
                ));
        }
    }
}
