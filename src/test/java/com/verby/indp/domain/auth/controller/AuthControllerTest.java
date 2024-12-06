package com.verby.indp.domain.auth.controller;

import static com.verby.indp.domain.auth.fixture.AdminFixture.admin;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.auth.dto.request.LoginRequest;
import com.verby.indp.domain.auth.dto.response.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class AuthControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공 : 로그인을 한다.")
    void login() throws Exception {
        // given
        Admin admin = admin();

        LoginRequest request = new LoginRequest(admin.getUserId(), admin.getPassword());
        LoginResponse response = new LoginResponse("token");

        when(authService.login(request)).thenReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/admin/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("userId").type(STRING).description("관리자 아이디"),
                        fieldWithPath("password").type(STRING).description("관리자 비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("accessToken").type(STRING).description("액세스 토큰")
                    )
                )
            );
    }

}
