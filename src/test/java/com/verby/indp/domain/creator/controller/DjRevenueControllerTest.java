package com.verby.indp.domain.creator.controller;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.request.RequestPayoutRequest;
import com.verby.indp.domain.creator.dto.response.DjRevenueResponse;
import com.verby.indp.domain.settlement.TaxType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class DjRevenueControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/dj/revenue 실행 시")
    class GetRevenue {

        @Test
        @DisplayName("성공 : 정산 정보를 반환한다.")
        void getRevenue() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            given(djRevenueService.getRevenue(any())).willReturn(new DjRevenueResponse(
                100L, 0L, 60_000L, false, 50_000L, false,
                new DjRevenueResponse.LastSettlement(
                    "PAID", 60_000L, LocalDateTime.of(2026, 6, 25, 10, 0))));

            ResultActions resultActions = mockMvc.perform(get("/api/dj/revenue")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("thisMonthEstimate").type(NUMBER).description("당월 실시간 예상 적립액 (원)"),
                        fieldWithPath("balance").type(NUMBER).description("현재 적립 잔액 (원)"),
                        fieldWithPath("totalPaid").type(NUMBER).description("누적 지급액 (원, 지급 완료된 신청 금액 합)"),
                        fieldWithPath("canRequest").type(BOOLEAN).description("정산 신청 가능 여부 (잔액 ≥ 최소 신청 금액 그리고 대기 신청 없음)"),
                        fieldWithPath("minPayout").type(NUMBER).description("최소 신청 금액 (원)"),
                        fieldWithPath("hasPendingRequest").type(BOOLEAN).description("처리 대기 중인 정산 신청 존재 여부"),
                        fieldWithPath("lastSettlement").type(OBJECT).optional().description("최근 처리된 정산 결과 (없으면 null)"),
                        fieldWithPath("lastSettlement.status").type(STRING).description("처리 상태 (PAID=지급 완료 / REJECTED=반려)"),
                        fieldWithPath("lastSettlement.amount").type(NUMBER).description("정산 금액 (원)"),
                        fieldWithPath("lastSettlement.processedAt").type(STRING).description("처리 일시")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("POST /api/dj/revenue/request 실행 시")
    class RequestPayout {

        @Test
        @DisplayName("성공 : 세금·계좌 정보와 함께 정산을 신청한다.")
        void requestPayout() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            willDoNothing().given(djRevenueService).requestPayout(any(), any());
            RequestPayoutRequest request = new RequestPayoutRequest(
                TaxType.INDIVIDUAL, "국민은행", "12345678901234", "박완",
                "010-1234-5678", "dj@example.com",
                "박완", "9010101234567", "서울시 강남구 테헤란로 1",
                null, null, null, null, null, true);

            ResultActions resultActions = mockMvc.perform(post("/api/dj/revenue/request")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    requestFields(
                        fieldWithPath("taxType").type(STRING).description("세금 유형 (INDIVIDUAL=개인 원천징수 / BUSINESS=사업자 세금계산서)"),
                        fieldWithPath("bankName").type(STRING).description("정산 입금 은행명"),
                        fieldWithPath("accountNumber").type(STRING).description("정산 입금 계좌번호 (저장 시 암호화)"),
                        fieldWithPath("accountHolder").type(STRING).description("예금주"),
                        fieldWithPath("contact").type(STRING).description("연락처"),
                        fieldWithPath("email").type(STRING).optional().description("세금 서류 수신 이메일 (선택)"),
                        fieldWithPath("residentName").type(STRING).optional().description("성명 (개인)"),
                        fieldWithPath("residentNumber").type(STRING).optional().description("주민등록번호 13자리 (개인, 저장 시 암호화)"),
                        fieldWithPath("residentAddress").type(STRING).optional().description("주소 (개인)"),
                        fieldWithPath("businessName").type(STRING).optional().description("상호 (사업자)"),
                        fieldWithPath("businessNumber").type(STRING).optional().description("사업자등록번호 10자리 (사업자)"),
                        fieldWithPath("representativeName").type(STRING).optional().description("대표자명 (사업자)"),
                        fieldWithPath("businessType").type(STRING).optional().description("업태/종목 (사업자, 선택)"),
                        fieldWithPath("businessAddress").type(STRING).optional().description("사업장 주소 (사업자)"),
                        fieldWithPath("privacyConsent").type(BOOLEAN).description("개인정보 수집·이용 동의 여부 (필수 true)")
                    )
                ));
        }
    }
}
