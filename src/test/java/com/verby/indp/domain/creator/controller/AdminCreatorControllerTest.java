package com.verby.indp.domain.creator.controller;

import static com.verby.indp.fixture.AdminFixture.admin;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.common.exception.ConflictException;
import com.verby.indp.domain.creator.dto.request.CreateCreatorRequest;
import com.verby.indp.domain.creator.dto.response.FindCreatorsResponse;
import com.verby.indp.domain.creator.dto.response.FindCreatorsResponse.CreatorItem;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class AdminCreatorControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("POST /api/admin/creators 실행 시")
    class CreateCreator {

        @Test
        @DisplayName("성공 : 크리에이터 계정을 생성한다.")
        void createCreator() throws Exception {
            // given
            Admin admin = admin();
            givenAdminAuth(admin);
            willDoNothing().given(adminCreatorService).createCreator(any());

            CreateCreatorRequest request = new CreateCreatorRequest(
                "박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "password123!");

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/admin/creators")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isCreated())
                .andDo(
                    restDocs.document(
                        requestFields(
                            fieldWithPath("name").type(STRING).description("실명"),
                            fieldWithPath("djName").type(STRING).description("크리에이터 활동명"),
                            fieldWithPath("phone").type(STRING).description("휴대폰 번호"),
                            fieldWithPath("email").type(STRING).description("로그인 이메일"),
                            fieldWithPath("password").type(STRING).description("비밀번호 (8자 이상)")
                        )
                    )
                );
        }

        @Test
        @DisplayName("실패 : 이미 사용 중인 이메일이면 409를 반환한다.")
        void createCreatorWithDuplicateEmail() throws Exception {
            // given
            Admin admin = admin();
            givenAdminAuth(admin);
            willThrow(new ConflictException("이미 사용 중인 이메일입니다."))
                .given(adminCreatorService).createCreator(any());

            CreateCreatorRequest request = new CreateCreatorRequest(
                "박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "password123!");

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/admin/creators")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("GET /api/admin/creators 실행 시")
    class FindCreators {

        @Test
        @DisplayName("성공 : 크리에이터 목록을 조회한다.")
        void findCreators() throws Exception {
            // given
            Admin admin = admin();
            givenAdminAuth(admin);

            CreatorItem item = new CreatorItem(
                1L, "박완", "DJ Parkwan", "010-1234-5678", "dj@example.com",
                null, LocalDateTime.of(2026, 6, 1, 0, 0), true,
                false, 0, 0, null, null, null, null, 6912L, 2.0);
            given(adminCreatorService.findCreators())
                .willReturn(new FindCreatorsResponse(List.of(item)));

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/admin/creators")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        responseFields(
                            fieldWithPath("creators").type(ARRAY).description("크리에이터 목록"),
                            fieldWithPath("creators[].id").type(NUMBER).description("크리에이터 ID"),
                            fieldWithPath("creators[].name").type(STRING).description("실명"),
                            fieldWithPath("creators[].djName").type(STRING).description("활동명"),
                            fieldWithPath("creators[].phone").type(STRING).description("휴대폰"),
                            fieldWithPath("creators[].email").type(STRING).description("이메일"),
                            fieldWithPath("creators[].thumbnailUrl").type(NULL).description("썸네일 URL"),
                            fieldWithPath("creators[].createdAt").type(STRING).description("생성일"),
                            fieldWithPath("creators[].active").type(BOOLEAN).description("활성 여부"),
                            fieldWithPath("creators[].isLive").type(BOOLEAN).description("라이브 여부"),
                            fieldWithPath("creators[].listenerCount").type(NUMBER).description("현재 청취자 수"),
                            fieldWithPath("creators[].trackCount").type(NUMBER).description("트랙 수"),
                            fieldWithPath("creators[].totalListenMinutes").type(NULL).description("누적 청취 시간(분)"),
                            fieldWithPath("creators[].subscriberCount").type(NULL).description("구독자 수"),
                            fieldWithPath("creators[].thisMonthEstimate").type(NULL).description("이번 달 예상 정산"),
                            fieldWithPath("creators[].totalPaid").type(NULL).description("누적 정산 금액"),
                            fieldWithPath("creators[].totalListenSeconds").type(NUMBER).description("누적 청취 시간(초) — 어드민 전용"),
                            fieldWithPath("creators[].accruedWon").type(NUMBER).description("누적 정산 적립액(원, 소수점) — 어드민 전용")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("PATCH /api/admin/creators/{creatorId}/deactivate 실행 시")
    class Deactivate {

        @Test
        @DisplayName("성공 : 크리에이터를 비활성화한다.")
        void deactivate() throws Exception {
            // given
            Admin admin = admin();
            givenAdminAuth(admin);
            willDoNothing().given(adminCreatorService).deactivate(1L);

            // when
            ResultActions resultActions = mockMvc.perform(
                patch("/api/admin/creators/{creatorId}/deactivate", 1L)
                    .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("creatorId").description("크리에이터 ID")
                        )
                    )
                );
        }
    }
}
