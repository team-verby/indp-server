package com.verby.indp.domain.creator.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.creator.dto.request.CreatorApplicationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class CreatorApplicationControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("POST /api/creator-applications 실행 시")
    class Apply {

        @Test
        @DisplayName("성공 : DJ 크리에이터 지원을 접수한다.")
        void apply() throws Exception {
            // given
            willDoNothing().given(creatorApplicationService).apply(any());

            CreatorApplicationRequest request = new CreatorApplicationRequest(
                "박완", "010-1234-5678", "dj@example.com",
                "https://youtube.com/playlist?list=abc", "신나는 음악", true);

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/creator-applications")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        requestFields(
                            fieldWithPath("name").type(STRING).description("지원자 성명"),
                            fieldWithPath("phone").type(STRING).description("연락처(휴대폰)"),
                            fieldWithPath("email").type(STRING).description("회신용 이메일"),
                            fieldWithPath("youtubeUrl").type(STRING).description("유튜브 플레이리스트 URL"),
                            fieldWithPath("introduction").type(STRING).optional().description("간단 소개 (선택)"),
                            fieldWithPath("privacyConsent").type(BOOLEAN).description("개인정보 수집·이용 동의 여부")
                        )
                    )
                );
        }
    }
}
