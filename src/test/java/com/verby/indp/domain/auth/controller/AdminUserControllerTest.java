package com.verby.indp.domain.auth.controller;

import static com.verby.indp.fixture.AdminFixture.admin;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.auth.dto.response.FindUsersResponse;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class AdminUserControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/admin/users 실행 시")
    class FindUsers {

        @Test
        @DisplayName("성공 : Plan A 구독자 목록을 반환한다.")
        void findUsers() throws Exception {
            Admin admin = admin();
            givenAdminAuth(admin);
            given(adminUserService.findUsers()).willReturn(new FindUsersResponse(
                List.of(new FindUsersResponse.UserItem(
                    1L, "user123", "홍길동", "user@test.com", "ACTIVE", LocalDate.of(2026, 7, 14), 4400, 1
                ))
            ));

            ResultActions resultActions = mockMvc.perform(get("/api/admin/users")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("users").type(ARRAY).description("구독자 목록"),
                        fieldWithPath("users[].userId").type(NUMBER).description("사용자 ID"),
                        fieldWithPath("users[].loginId").type(STRING).description("아이디"),
                        fieldWithPath("users[].name").type(STRING).description("성함"),
                        fieldWithPath("users[].email").type(STRING).description("이메일"),
                        fieldWithPath("users[].subscriptionStatus").type(STRING).description("구독 상태"),
                        fieldWithPath("users[].subscriptionEndDate").type(STRING).description("구독 만료일 (없으면 null)").optional(),
                        fieldWithPath("users[].paidAmount").type(NUMBER).description("결제 금액 (없으면 null)").optional(),
                        fieldWithPath("users[].usagePeriod").type(NUMBER).description("이용 기간 (개월, 없으면 null)").optional()
                    )
                ));
        }
    }
}
