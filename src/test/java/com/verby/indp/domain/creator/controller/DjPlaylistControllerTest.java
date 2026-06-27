package com.verby.indp.domain.creator.controller;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
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
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.response.DjPlaylistDetailResponse;
import com.verby.indp.domain.creator.dto.response.FindDjPlaylistsResponse;
import com.verby.indp.domain.creator.dto.response.FindDjPlaylistsResponse.DjPlaylistItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class DjPlaylistControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/dj/playlists 실행 시")
    class GetPlaylists {

        @Test
        @DisplayName("성공 : DJ 채널 목록을 반환한다.")
        void getPlaylists() throws Exception {
            given(djPlaylistService.getPlaylists())
                .willReturn(new FindDjPlaylistsResponse(
                    List.of(new DjPlaylistItem(
                        1L, "DJ Parkwan 채널", "DJ Parkwan", "https://cdn.example.com/thumb.jpg", true, 7))
                ));

            ResultActions resultActions = mockMvc.perform(get("/api/dj/playlists"));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("playlists").type(ARRAY).description("DJ 채널 목록"),
                        fieldWithPath("playlists[].id").type(NUMBER).description("크리에이터 ID"),
                        fieldWithPath("playlists[].name").type(STRING).description("채널명"),
                        fieldWithPath("playlists[].djName").type(STRING).description("DJ 활동명"),
                        fieldWithPath("playlists[].thumbnailUrl").description("썸네일 URL (null 가능)"),
                        fieldWithPath("playlists[].isLive").type(BOOLEAN).description("라이브 여부"),
                        fieldWithPath("playlists[].listeners").type(NUMBER).description("현재 청취자 수 (비라이브는 0)")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("GET /api/dj/playlists/{creatorId} 실행 시")
    class GetPlaylistDetail {

        @Test
        @DisplayName("성공 : DJ 채널 상세를 반환한다.")
        void getPlaylistDetail() throws Exception {
            given(djPlaylistService.getPlaylistDetail(1L))
                .willReturn(new DjPlaylistDetailResponse(
                    1L, "DJ Parkwan 채널", "DJ Parkwan", "https://cdn.example.com/thumb.jpg",
                    "잔잔한 카페 음악을 들려드립니다.", true, 0, 0, List.of()));

            ResultActions resultActions = mockMvc.perform(get("/api/dj/playlists/{creatorId}", 1L));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    pathParameters(
                        parameterWithName("creatorId").description("크리에이터 ID")
                    ),
                    responseFields(
                        fieldWithPath("id").type(NUMBER).description("크리에이터 ID"),
                        fieldWithPath("name").type(STRING).description("채널명"),
                        fieldWithPath("djName").type(STRING).description("DJ 활동명"),
                        fieldWithPath("thumbnailUrl").description("썸네일 URL (null 가능)"),
                        fieldWithPath("introduction").description("소개글 (null 가능)"),
                        fieldWithPath("isLive").type(BOOLEAN).description("라이브 여부"),
                        fieldWithPath("listeners").type(NUMBER).description("현재 청취자 수"),
                        fieldWithPath("currentIdx").type(NUMBER).description("현재 재생 트랙 인덱스"),
                        fieldWithPath("tracks").type(ARRAY).description("트랙 목록")
                    )
                ));
        }
    }
}
