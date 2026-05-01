package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.playlist.dto.response.FindStorePlaylistByOwnerResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static com.verby.indp.fixture.OwnerFixture.owner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OwnerPlaylistControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/owner/stores/{storeId}/playlists 실행 시")
    class FindStorePlaylist {

        @Test
        @DisplayName("성공 : 점주가 매장 플레이리스트를 조회한다.")
        void findStorePlaylist() throws Exception {
            // given
            Owner owner = owner();
            givenOwnerAuth(owner);

            FindStorePlaylistByOwnerResponse response =
                new FindStorePlaylistByOwnerResponse(null, null);
            given(ownerPlaylistService.getStorePlaylist(any(), eq(1L))).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(
                get("/api/owner/stores/{storeId}/playlists", 1L)
                    .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("storeId").description("매장 ID")
                        ),
                        responseFields(
                            fieldWithPath("playlist").type(NULL)
                                .description("플레이리스트 정보. 플레이리스트가 없으면 null"),
                            fieldWithPath("currentSong").type(NULL)
                                .description("현재 재생 중인 곡. 재생 중인 곡이 없으면 null")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("POST /api/owner/stores/{storeId}/playlists/regenerations 실행 시")
    class RegeneratePlaylist {

        @Test
        @DisplayName("성공 : 점주가 플레이리스트 재생성을 요청한다.")
        void regeneratePlaylist() throws Exception {
            // given
            Owner owner = owner();
            givenOwnerAuth(owner);
            willDoNothing().given(ownerPlaylistService).regeneratePlaylist(any(), eq(1L));

            // when
            ResultActions resultActions = mockMvc.perform(
                post("/api/owner/stores/{storeId}/playlists/regenerations", 1L)
                    .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("storeId").description("매장 ID")
                        )
                    )
                );
        }
    }
}
