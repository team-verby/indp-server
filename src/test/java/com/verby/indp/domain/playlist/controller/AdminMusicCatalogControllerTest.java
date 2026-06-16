package com.verby.indp.domain.playlist.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.playlist.dto.request.UpdateMusicCatalogRequest;
import com.verby.indp.domain.playlist.dto.request.UpdateMusicCatalogRequest.MoodCatalog;
import com.verby.indp.domain.playlist.dto.request.UpdateMusicCatalogRequest.SongItem;
import com.verby.indp.domain.playlist.dto.response.FindMusicCatalogResponse;
import com.verby.indp.fixture.AdminFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class AdminMusicCatalogControllerTest extends BaseControllerTest {

    private Admin admin() {
        return AdminFixture.admin();
    }

    @Nested
    @DisplayName("GET /api/admin/music-catalog 실행 시")
    class FindMusicCatalog {

        @Test
        @DisplayName("성공 : 무드별 음원 카탈로그를 조회한다.")
        void findMusicCatalog() throws Exception {
            // given
            givenAdminAuth(admin());
            given(musicCatalogService.findMusicCatalog()).willReturn(
                new FindMusicCatalogResponse(LocalDateTime.of(2026, 6, 16, 15, 30, 0), List.of(
                    new FindMusicCatalogResponse.MoodCatalog("잔잔한", List.of(
                        new FindMusicCatalogResponse.SongItem(
                            "안녕 나의 사랑", "성시경", "4:19", "5zAEiu3SaO4")
                    ))
                )));

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/admin/music-catalog")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("savedAt").type(STRING)
                            .description("마지막 저장 시각(ISO-8601). 저장된 적 없으면 null").optional(),
                        fieldWithPath("moods").type(ARRAY).description("무드 목록"),
                        fieldWithPath("moods[].mood").type(STRING).description("무드명"),
                        fieldWithPath("moods[].songs").type(ARRAY).description("해당 무드의 곡 목록"),
                        fieldWithPath("moods[].songs[].title").type(STRING).description("곡 제목"),
                        fieldWithPath("moods[].songs[].artist").type(STRING)
                            .description("아티스트").optional(),
                        fieldWithPath("moods[].songs[].playTime").type(STRING)
                            .description("재생시간(MM:SS)").optional(),
                        fieldWithPath("moods[].songs[].vid").type(STRING)
                            .description("YouTube 영상 ID").optional()
                    )
                ));
        }
    }

    @Nested
    @DisplayName("PUT /api/admin/music-catalog 실행 시")
    class UpdateMusicCatalog {

        @Test
        @DisplayName("성공 : 음원 카탈로그 전체를 저장(교체)하고 저장 시각을 반환한다.")
        void updateMusicCatalog() throws Exception {
            // given
            givenAdminAuth(admin());
            given(musicCatalogService.updateMusicCatalog(any()))
                .willReturn(LocalDateTime.of(2026, 6, 16, 15, 30, 0));

            UpdateMusicCatalogRequest request = new UpdateMusicCatalogRequest(List.of(
                new MoodCatalog("잔잔한", List.of(
                    new SongItem("안녕 나의 사랑", "성시경", "4:19", "5zAEiu3SaO4")
                ))
            ));

            // when
            ResultActions resultActions = mockMvc.perform(put("/api/admin/music-catalog")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    requestFields(
                        fieldWithPath("moods").type(ARRAY).description("무드 목록"),
                        fieldWithPath("moods[].mood").type(STRING).description("무드명"),
                        fieldWithPath("moods[].songs").type(ARRAY).description("해당 무드의 곡 목록"),
                        fieldWithPath("moods[].songs[].title").type(STRING).description("곡 제목"),
                        fieldWithPath("moods[].songs[].artist").type(STRING)
                            .description("아티스트").optional(),
                        fieldWithPath("moods[].songs[].playTime").type(STRING)
                            .description("재생시간(MM:SS)").optional(),
                        fieldWithPath("moods[].songs[].vid").type(STRING)
                            .description("YouTube 영상 ID").optional()
                    ),
                    responseFields(
                        fieldWithPath("savedAt").type(STRING)
                            .description("저장 시각(ISO-8601). 저장된 곡이 없으면 null").optional()
                    )
                ));
        }
    }
}
