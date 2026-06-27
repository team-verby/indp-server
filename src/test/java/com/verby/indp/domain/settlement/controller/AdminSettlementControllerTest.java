package com.verby.indp.domain.settlement.controller;

import static com.verby.indp.fixture.AdminFixture.admin;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.settlement.SettlementStatus;
import com.verby.indp.domain.settlement.TaxType;
import com.verby.indp.domain.settlement.dto.response.FindSettlementsResponse;
import com.verby.indp.domain.settlement.dto.response.FindSettlementsResponse.SettlementItem;
import com.verby.indp.domain.settlement.dto.response.FindSettlementsResponse.TaxInfoSummary;
import com.verby.indp.domain.settlement.dto.response.SettlementTaxSecretResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class AdminSettlementControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/admin/settlements 실행 시")
    class FindSettlements {

        @Test
        @DisplayName("성공 : 정산 신청 목록을 반환한다.")
        void findSettlements() throws Exception {
            givenAdminAuth(admin());
            TaxInfoSummary taxInfo = new TaxInfoSummary(
                TaxType.INDIVIDUAL, "박완", "901010-1******", "서울시 강남구",
                null, null, null, null, null,
                "국민은행", "123*******34", "박완",
                "010-1234-5678", "dj@example.com", LocalDateTime.of(2026, 6, 18, 10, 0));
            FindSettlementsResponse response = new FindSettlementsResponse(List.of(
                new SettlementItem(
                    1L, 10L, "DJ Parkwan", 60_000L, SettlementStatus.PAID,
                    LocalDateTime.of(2026, 6, 18, 10, 0), LocalDateTime.of(2026, 6, 19, 9, 0),
                    taxInfo)));
            given(adminSettlementService.findSettlements(any())).willReturn(response);

            ResultActions resultActions = mockMvc.perform(get("/api/admin/settlements")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("settlements[].settlementRequestId").type(NUMBER).description("정산 신청 ID"),
                        fieldWithPath("settlements[].creatorId").type(NUMBER).description("크리에이터 ID"),
                        fieldWithPath("settlements[].djName").type(STRING).description("DJ(채널)명"),
                        fieldWithPath("settlements[].amount").type(NUMBER).description("신청 금액 (원)"),
                        fieldWithPath("settlements[].status").type(STRING).description("상태 (REQUESTED/PAID/REJECTED)"),
                        fieldWithPath("settlements[].requestedAt").type(STRING).description("신청 일시"),
                        fieldWithPath("settlements[].processedAt").type(STRING).optional().description("처리 일시 (미처리 시 null)"),
                        fieldWithPath("settlements[].taxInfo").type("Object").optional().description("세금·신원·계좌 정보 요약 (구버전 신청은 null)"),
                        fieldWithPath("settlements[].taxInfo.taxType").type(STRING).description("세금 유형 (INDIVIDUAL/BUSINESS)"),
                        fieldWithPath("settlements[].taxInfo.displayName").type(STRING).optional().description("표시 이름 (개인=성명 / 사업자=대표자명)"),
                        fieldWithPath("settlements[].taxInfo.maskedResidentNumber").type(STRING).optional().description("마스킹된 주민등록번호 (개인)"),
                        fieldWithPath("settlements[].taxInfo.residentAddress").type(STRING).optional().description("주소 (개인)"),
                        fieldWithPath("settlements[].taxInfo.businessName").type(STRING).optional().description("상호 (사업자)"),
                        fieldWithPath("settlements[].taxInfo.businessNumber").type(STRING).optional().description("사업자등록번호 (사업자)"),
                        fieldWithPath("settlements[].taxInfo.representativeName").type(STRING).optional().description("대표자명 (사업자)"),
                        fieldWithPath("settlements[].taxInfo.businessType").type(STRING).optional().description("업태/종목 (사업자)"),
                        fieldWithPath("settlements[].taxInfo.businessAddress").type(STRING).optional().description("사업장 주소 (사업자)"),
                        fieldWithPath("settlements[].taxInfo.bankName").type(STRING).optional().description("은행명"),
                        fieldWithPath("settlements[].taxInfo.maskedAccountNumber").type(STRING).optional().description("마스킹된 계좌번호"),
                        fieldWithPath("settlements[].taxInfo.accountHolder").type(STRING).optional().description("예금주"),
                        fieldWithPath("settlements[].taxInfo.contact").type(STRING).optional().description("연락처"),
                        fieldWithPath("settlements[].taxInfo.email").type(STRING).optional().description("세금 서류 수신 이메일"),
                        fieldWithPath("settlements[].taxInfo.privacyConsentAt").type(STRING).optional().description("개인정보 수집·이용 동의 일시")
                    )
                ));
        }

        @Test
        @DisplayName("성공 : 정산 신청의 민감정보 전체 값을 복호화해 반환한다.")
        void getTaxSecret() throws Exception {
            givenAdminAuth(admin());
            given(adminSettlementService.getTaxSecret(anyLong()))
                .willReturn(new SettlementTaxSecretResponse("9010101234567", "12345678901234"));

            ResultActions resultActions = mockMvc.perform(
                get("/api/admin/settlements/{settlementRequestId}/tax-info", 1L)
                    .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("residentNumber").type(STRING).optional().description("주민등록번호 전체 (개인, 없으면 null)"),
                        fieldWithPath("accountNumber").type(STRING).optional().description("계좌번호 전체")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("PATCH /api/admin/settlements/{id}/approve 실행 시")
    class Approve {

        @Test
        @DisplayName("성공 : 정산 신청을 지급 처리한다.")
        void approve() throws Exception {
            givenAdminAuth(admin());
            willDoNothing().given(adminSettlementService).approve(anyLong());

            ResultActions resultActions = mockMvc.perform(patch("/api/admin/settlements/{id}/approve", 1L)
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("PATCH /api/admin/settlements/{id}/reject 실행 시")
    class Reject {

        @Test
        @DisplayName("성공 : 정산 신청을 반려한다.")
        void reject() throws Exception {
            givenAdminAuth(admin());
            willDoNothing().given(adminSettlementService).reject(anyLong());

            ResultActions resultActions = mockMvc.perform(patch("/api/admin/settlements/{id}/reject", 1L)
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk());
        }
    }
}
