package com.verby.indp.domain.creator.controller;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.request.UpdateLiveTracksRequest;
import com.verby.indp.domain.creator.dto.response.DjLiveListenersResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class DjLiveControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("POST /api/dj/live/start 실행 시")
    class StartLive {

        @Test
        @DisplayName("성공 : 라이브를 시작한다.")
        void startLive() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            willDoNothing().given(djLiveService).startLive(any());

            ResultActions resultActions = mockMvc.perform(post("/api/dj/live/start")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk());
        }

        @Test
        @DisplayName("실패 : 트랙이 없으면 400을 반환한다.")
        void startLiveNoTracks() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            willThrow(new BadRequestException("업로드된 트랙이 없으면 라이브를 시작할 수 없습니다."))
                .given(djLiveService).startLive(any());

            ResultActions resultActions = mockMvc.perform(post("/api/dj/live/start")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/dj/live/stop 실행 시")
    class StopLive {

        @Test
        @DisplayName("성공 : 라이브를 종료한다.")
        void stopLive() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            willDoNothing().given(djLiveService).stopLive(any());

            ResultActions resultActions = mockMvc.perform(post("/api/dj/live/stop")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("GET /api/dj/live/listeners 실행 시")
    class GetListeners {

        @Test
        @DisplayName("성공 : 청취자 수를 반환한다.")
        void getListeners() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            given(djLiveService.getListeners(any())).willReturn(new DjLiveListenersResponse(0));

            ResultActions resultActions = mockMvc.perform(get("/api/dj/live/listeners")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(fieldWithPath("count").type(NUMBER).description("현재 청취자 수"))
                ));
        }
    }

    @Nested
    @DisplayName("PUT /api/dj/live/tracks 실행 시")
    class UpdateLiveTracks {

        @Test
        @DisplayName("성공 : 라이브 트랙을 업데이트한다.")
        void updateLiveTracks() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            willDoNothing().given(djLiveService).updateLiveTracks(any(), any());

            UpdateLiveTracksRequest request = new UpdateLiveTracksRequest(List.of(1L, 2L, 3L));

            ResultActions resultActions = mockMvc.perform(put("/api/dj/live/tracks")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            resultActions.andExpect(status().isOk());
        }
    }
}
