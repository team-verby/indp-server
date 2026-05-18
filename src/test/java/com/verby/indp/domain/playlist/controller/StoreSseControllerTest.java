package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StoreSseControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/stores/{storeId}/sse 실행 시")
    class Subscribe {

        @Test
        @DisplayName("성공 : SSE 구독에 성공한다.")
        void subscribe() throws Exception {
            // given
            given(storeSseService.subscribe(1L)).willReturn(new SseEmitter());

            // when
            ResultActions resultActions = mockMvc.perform(
                get("/api/stores/{storeId}/sse", 1L)
                    .accept(MediaType.TEXT_EVENT_STREAM));

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
