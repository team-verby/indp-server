package com.verby.indp.domain.playlist.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.playlist.dto.request.UpdateMoodScheduleRequest;
import com.verby.indp.domain.playlist.dto.request.UpdateMoodScheduleRequest.Slot;
import com.verby.indp.domain.playlist.dto.request.UpdateMoodScheduleRequest.StoreSchedule;
import com.verby.indp.domain.playlist.dto.response.FindMoodScheduleResponse;
import com.verby.indp.fixture.AdminFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class AdminMoodScheduleControllerTest extends BaseControllerTest {

    private Admin admin() {
        return AdminFixture.admin();
    }

    @Nested
    @DisplayName("GET /api/admin/mood-schedule 실행 시")
    class FindMoodSchedule {

        @Test
        @DisplayName("성공 : 매장별 시간대-무드 매핑을 조회한다.")
        void findMoodSchedule() throws Exception {
            // given
            givenAdminAuth(admin());
            given(moodScheduleService.findMoodSchedule()).willReturn(
                new FindMoodScheduleResponse(LocalDateTime.of(2026, 6, 19, 3, 0, 0), List.of(
                    new FindMoodScheduleResponse.StoreSchedule("카페 공명 홍대점", List.of(
                        new FindMoodScheduleResponse.Slot(10, "모던한 팝")
                    ))
                )));

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/admin/mood-schedule")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("savedAt").type(STRING)
                            .description("마지막 저장 시각(ISO-8601). 저장된 적 없으면 null").optional(),
                        fieldWithPath("schedules").type(ARRAY).description("매장별 매핑 목록"),
                        fieldWithPath("schedules[].storeName").type(STRING).description("매장명"),
                        fieldWithPath("schedules[].slots").type(ARRAY).description("시간대별 무드 목록"),
                        fieldWithPath("schedules[].slots[].hour").type(NUMBER)
                            .description("시간대(0~23)"),
                        fieldWithPath("schedules[].slots[].mood").type(STRING).description("무드명")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("PUT /api/admin/mood-schedule 실행 시")
    class UpdateMoodSchedule {

        @Test
        @DisplayName("성공 : 매장별 매핑 전체를 저장(교체)하고 저장 시각을 반환한다.")
        void updateMoodSchedule() throws Exception {
            // given
            givenAdminAuth(admin());
            given(moodScheduleService.updateMoodSchedule(any()))
                .willReturn(LocalDateTime.of(2026, 6, 19, 3, 0, 0));

            UpdateMoodScheduleRequest request = new UpdateMoodScheduleRequest(List.of(
                new StoreSchedule("카페 공명 홍대점", List.of(
                    new Slot(10, "모던한 팝")
                ))
            ));

            // when
            ResultActions resultActions = mockMvc.perform(put("/api/admin/mood-schedule")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    requestFields(
                        fieldWithPath("schedules").type(ARRAY).description("매장별 매핑 목록"),
                        fieldWithPath("schedules[].storeName").type(STRING).description("매장명"),
                        fieldWithPath("schedules[].slots").type(ARRAY).description("시간대별 무드 목록"),
                        fieldWithPath("schedules[].slots[].hour").type(NUMBER)
                            .description("시간대(0~23)"),
                        fieldWithPath("schedules[].slots[].mood").type(STRING).description("무드명")
                    ),
                    responseFields(
                        fieldWithPath("savedAt").type(STRING)
                            .description("저장 시각(ISO-8601). 저장된 행이 없으면 null").optional()
                    )
                ));
        }
    }
}
