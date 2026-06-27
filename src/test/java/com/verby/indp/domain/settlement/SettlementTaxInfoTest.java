package com.verby.indp.domain.settlement;

import static org.assertj.core.api.Assertions.assertThat;

import com.verby.indp.domain.creator.dto.request.RequestPayoutRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SettlementTaxInfoTest {

    private static final LocalDateTime CONSENT_AT = LocalDateTime.of(2026, 6, 18, 10, 0);

    @Test
    @DisplayName("성공 : 개인 신청 요청의 모든 필드를 스냅샷으로 복사한다.")
    void fromIndividual() {
        RequestPayoutRequest request = new RequestPayoutRequest(
            TaxType.INDIVIDUAL, "국민은행", "12345678901234", "박완",
            "010-1234-5678", "dj@example.com",
            "박완", "9010101234567", "서울시 강남구",
            null, null, null, null, null, true);

        SettlementTaxInfo info = SettlementTaxInfo.from(request, CONSENT_AT);

        assertThat(info.getTaxType()).isEqualTo(TaxType.INDIVIDUAL);
        assertThat(info.getBankName()).isEqualTo("국민은행");
        assertThat(info.getAccountNumber()).isEqualTo("12345678901234");
        assertThat(info.getAccountHolder()).isEqualTo("박완");
        assertThat(info.getContact()).isEqualTo("010-1234-5678");
        assertThat(info.getEmail()).isEqualTo("dj@example.com");
        assertThat(info.getResidentName()).isEqualTo("박완");
        assertThat(info.getResidentNumber()).isEqualTo("9010101234567");
        assertThat(info.getResidentAddress()).isEqualTo("서울시 강남구");
        assertThat(info.getBusinessName()).isNull();
        assertThat(info.getBusinessNumber()).isNull();
        assertThat(info.getRepresentativeName()).isNull();
        assertThat(info.getBusinessType()).isNull();
        assertThat(info.getBusinessAddress()).isNull();
        assertThat(info.isPrivacyConsent()).isTrue();
        assertThat(info.getPrivacyConsentAt()).isEqualTo(CONSENT_AT);
    }

    @Test
    @DisplayName("성공 : 사업자 신청 요청의 사업자 필드를 스냅샷으로 복사한다.")
    void fromBusiness() {
        RequestPayoutRequest request = new RequestPayoutRequest(
            TaxType.BUSINESS, "신한은행", "98765432109876", "(주)버비",
            "02-123-4567", "biz@example.com",
            null, null, null,
            "(주)버비", "1234567890", "박대표", "서비스/소프트웨어", "서울시 서초구",
            true);

        SettlementTaxInfo info = SettlementTaxInfo.from(request, CONSENT_AT);

        assertThat(info.getTaxType()).isEqualTo(TaxType.BUSINESS);
        assertThat(info.getBusinessName()).isEqualTo("(주)버비");
        assertThat(info.getBusinessNumber()).isEqualTo("1234567890");
        assertThat(info.getRepresentativeName()).isEqualTo("박대표");
        assertThat(info.getBusinessType()).isEqualTo("서비스/소프트웨어");
        assertThat(info.getBusinessAddress()).isEqualTo("서울시 서초구");
        assertThat(info.getResidentNumber()).isNull();
    }
}
