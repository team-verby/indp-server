package com.verby.indp.domain.listening.controller;

import static com.verby.indp.fixture.UserFixture.userWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.listening.dto.request.HeartbeatRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class ListeningControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("POST /api/listening/heartbeat 실행 시")
    class Heartbeat {

        @Test
        @DisplayName("성공 : 청취 델타를 기록하고 200을 반환한다.")
        void heartbeat() throws Exception {
            User user = userWithId(1L);
            givenUserAuth(user);
            willDoNothing().given(listeningService).heartbeat(any(), any());

            String body = objectMapper.writeValueAsString(new HeartbeatRequest(10L, 300));

            ResultActions resultActions = mockMvc.perform(post("/api/listening/heartbeat")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    requestFields(
                        fieldWithPath("creatorId").type(NUMBER).description("청취 대상 크리에이터 ID"),
                        fieldWithPath("seconds").type(NUMBER).description("직전 전송 이후 추가 재생 초 (델타)")
                    )
                ));
        }
    }
}
