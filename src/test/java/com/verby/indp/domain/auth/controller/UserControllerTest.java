package com.verby.indp.domain.auth.controller;

import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/user/check-id 실행 시")
    class CheckId {

        @Test
        @DisplayName("성공 : 사용 가능한 아이디이면 200을 반환한다.")
        void checkId() throws Exception {
            // given
            willDoNothing().given(userService).checkLoginIdDuplicate("newuser123");

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/user/check-id")
                .param("loginId", "newuser123"));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        queryParameters(
                            parameterWithName("loginId").description("중복 확인할 아이디")
                        )
                    )
                );
        }

        @Test
        @DisplayName("실패 : 이미 사용 중인 아이디이면 400을 반환한다.")
        void checkIdWithDuplicate() throws Exception {
            willThrow(new BadRequestException("이미 사용 중인 아이디입니다."))
                .given(userService).checkLoginIdDuplicate("parkwan123");

            ResultActions resultActions = mockMvc.perform(get("/api/user/check-id")
                .param("loginId", "parkwan123"));

            resultActions.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/user/check-email 실행 시")
    class CheckEmail {

        @Test
        @DisplayName("성공 : 사용 가능한 이메일이면 200을 반환한다.")
        void checkEmail() throws Exception {
            willDoNothing().given(userService).checkEmailDuplicate("new@example.com");

            ResultActions resultActions = mockMvc.perform(get("/api/user/check-email")
                .param("email", "new@example.com"));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    queryParameters(
                        parameterWithName("email").description("중복 확인할 이메일")
                    )
                ));
        }

        @Test
        @DisplayName("실패 : 이미 사용 중인 이메일이면 400을 반환한다.")
        void checkEmailWithDuplicate() throws Exception {
            willThrow(new BadRequestException("이미 사용 중인 이메일입니다."))
                .given(userService).checkEmailDuplicate("dup@example.com");

            ResultActions resultActions = mockMvc.perform(get("/api/user/check-email")
                .param("email", "dup@example.com"));

            resultActions.andExpect(status().isBadRequest());
        }
    }
}
