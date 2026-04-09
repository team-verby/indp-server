package com.verby.indp.domain.auth.controller;

import static com.verby.indp.fixture.OwnerFixture.owner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.dto.request.LoginRequest;
import com.verby.indp.domain.auth.dto.request.RefreshRequest;
import com.verby.indp.domain.auth.dto.response.LoginResponse;
import com.verby.indp.domain.auth.dto.response.RefreshResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class AuthControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("POST /api/admin/login 실행 시")
    class AdminLogin {

        @Test
        @DisplayName("성공 : 어드민이 로그인한다.")
        void adminLogin() throws Exception {
            // given
            LoginResponse response = new LoginResponse("access-token", "refresh-token");
            given(adminService.login(any())).willReturn(response);

            LoginRequest request = new LoginRequest("admin", "password123!");

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/admin/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        requestFields(
                            fieldWithPath("loginId").type(STRING).description("로그인 아이디"),
                            fieldWithPath("password").type(STRING).description("비밀번호")
                        ),
                        responseFields(
                            fieldWithPath("accessToken").type(STRING).description("액세스 토큰 (유효기간 1시간)"),
                            fieldWithPath("refreshToken").type(STRING).description("리프레시 토큰 (유효기간 30일)")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("POST /api/owner/login 실행 시")
    class OwnerLogin {

        @Test
        @DisplayName("성공 : 점주가 로그인한다.")
        void ownerLogin() throws Exception {
            // given
            LoginResponse response = new LoginResponse("access-token", "refresh-token");
            given(ownerService.login(any())).willReturn(response);

            LoginRequest request = new LoginRequest("store0001", "a3f9b2c1d0");

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/owner/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        requestFields(
                            fieldWithPath("loginId").type(STRING).description("로그인 아이디"),
                            fieldWithPath("password").type(STRING).description("비밀번호")
                        ),
                        responseFields(
                            fieldWithPath("accessToken").type(STRING).description("액세스 토큰 (유효기간 1시간)"),
                            fieldWithPath("refreshToken").type(STRING).description("리프레시 토큰 (유효기간 30일)")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("POST /api/auth/refresh 실행 시")
    class Refresh {

        @Test
        @DisplayName("성공 : 토큰을 재발급한다.")
        void refresh() throws Exception {
            // given
            RefreshResponse response = new RefreshResponse("new-access-token", "new-refresh-token");
            given(authTokenService.refresh(any())).willReturn(response);

            RefreshRequest request = new RefreshRequest("old-refresh-token");

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/auth/refresh")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        requestFields(
                            fieldWithPath("refreshToken").type(STRING).description("기존 리프레시 토큰")
                        ),
                        responseFields(
                            fieldWithPath("accessToken").type(STRING).description("새 액세스 토큰"),
                            fieldWithPath("refreshToken").type(STRING).description("새 리프레시 토큰")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("POST /api/owner/logout 실행 시")
    class OwnerLogout {

        @Test
        @DisplayName("성공 : 점주가 로그아웃한다.")
        void ownerLogout() throws Exception {
            // given
            Owner owner = owner();
            givenOwnerAuth(owner);
            willDoNothing().given(ownerService).logout(any());

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/owner/logout")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isNoContent())
                .andDo(restDocs.document());
        }
    }

    @Nested
    @DisplayName("POST /api/admin/logout 실행 시")
    class AdminLogout {

        @Test
        @DisplayName("성공 : 어드민이 로그아웃한다.")
        void adminLogout() throws Exception {
            // given
            com.verby.indp.domain.auth.Admin admin = org.mockito.Mockito.mock(
                com.verby.indp.domain.auth.Admin.class);
            given(admin.getAdminId()).willReturn(1L);
            givenAdminAuth(admin);
            willDoNothing().given(adminService).logout(any());

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/admin/logout")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isNoContent())
                .andDo(restDocs.document());
        }
    }
}
