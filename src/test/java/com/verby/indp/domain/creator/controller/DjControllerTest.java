package com.verby.indp.domain.creator.controller;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.common.exception.AuthException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.request.ChangePasswordRequest;
import com.verby.indp.domain.creator.dto.response.DjProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class DjControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/dj/profile 실행 시")
    class GetProfile {

        @Test
        @DisplayName("성공 : DJ 프로필을 반환한다.")
        void getProfile() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            given(djService.getProfile(creator))
                .willReturn(new DjProfileResponse(
                    "DJ Parkwan", "박완", "010-1234-5678", "dj@example.com",
                    "https://cdn.example.com/thumb.jpg", "잔잔한 카페 음악을 들려드립니다."));

            ResultActions resultActions = mockMvc.perform(get("/api/dj/profile")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("djName").type(STRING).description("활동명"),
                        fieldWithPath("name").type(STRING).description("실명"),
                        fieldWithPath("phone").type(STRING).description("휴대폰"),
                        fieldWithPath("email").type(STRING).description("이메일"),
                        fieldWithPath("thumbnailUrl").description("썸네일 URL (null 가능)"),
                        fieldWithPath("introduction").description("소개글 (null 가능)")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("PATCH /api/dj/profile 실행 시")
    class UpdateProfile {

        @Test
        @DisplayName("성공 : 프로필을 업데이트한다.")
        void updateProfile() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            willDoNothing().given(djService).updateProfile(any(), any());

            mockMvc.perform(
                    org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .multipart(org.springframework.http.HttpMethod.PATCH, "/api/dj/profile")
                        .param("djName", "DJ New")
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN))
                .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("PATCH /api/dj/password 실행 시")
    class ChangePassword {

        @Test
        @DisplayName("성공 : 비밀번호를 변경한다.")
        void changePassword() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            willDoNothing().given(djService).changePassword(any(), any());

            ChangePasswordRequest request = new ChangePasswordRequest("password123!", "newPassword1!");

            ResultActions resultActions = mockMvc.perform(patch("/api/dj/password")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    requestFields(
                        fieldWithPath("currentPassword").type(STRING).description("현재 비밀번호"),
                        fieldWithPath("newPassword").type(STRING).description("새 비밀번호")
                    )
                ));
        }

        @Test
        @DisplayName("실패 : 현재 비밀번호 불일치 시 401을 반환한다.")
        void changePasswordWithWrongCurrent() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            willThrow(new AuthException("현재 비밀번호가 일치하지 않습니다."))
                .given(djService).changePassword(any(), any());

            ChangePasswordRequest request = new ChangePasswordRequest("wrong", "newPassword1!");

            ResultActions resultActions = mockMvc.perform(patch("/api/dj/password")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            resultActions.andExpect(status().isUnauthorized());
        }
    }
}
