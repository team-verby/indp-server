package com.verby.indp.domain.settlement;

import com.verby.indp.domain.creator.dto.request.RequestPayoutRequest;
import com.verby.indp.global.crypto.EncryptedStringConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 정산 신청 시점에 수집한 세금·신원·계좌 정보 스냅샷.
 * 주민등록번호·계좌번호는 {@link EncryptedStringConverter}로 암호화 저장한다.
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SettlementTaxInfo {

    @Enumerated(EnumType.STRING)
    @Column(name = "tax_type")
    private TaxType taxType;

    // ── 공통: 정산 계좌 ──
    @Column(name = "bank_name")
    private String bankName;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "account_number", length = 512)
    private String accountNumber;

    @Column(name = "account_holder")
    private String accountHolder;

    // ── 공통: 연락 ──
    @Column(name = "tax_contact")
    private String contact;

    @Column(name = "tax_email")
    private String email;

    // ── 개인(원천징수) ──
    @Column(name = "resident_name")
    private String residentName;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "resident_number", length = 512)
    private String residentNumber;

    @Column(name = "resident_address", length = 500)
    private String residentAddress;

    // ── 사업자(세금계산서) ──
    @Column(name = "business_name")
    private String businessName;

    @Column(name = "business_number")
    private String businessNumber;

    @Column(name = "representative_name")
    private String representativeName;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "business_address", length = 500)
    private String businessAddress;

    // ── 개인정보 동의 ──
    @Column(name = "privacy_consent")
    private boolean privacyConsent;

    @Column(name = "privacy_consent_at")
    private LocalDateTime privacyConsentAt;

    /** 검증을 마친 신청 요청으로부터 스냅샷을 생성한다. (분기 없이 그대로 복사) */
    public static SettlementTaxInfo from(RequestPayoutRequest request, LocalDateTime consentAt) {
        SettlementTaxInfo info = new SettlementTaxInfo();
        info.taxType = request.taxType();
        info.bankName = request.bankName();
        info.accountNumber = request.accountNumber();
        info.accountHolder = request.accountHolder();
        info.contact = request.contact();
        info.email = request.email();
        info.residentName = request.residentName();
        info.residentNumber = request.residentNumber();
        info.residentAddress = request.residentAddress();
        info.businessName = request.businessName();
        info.businessNumber = request.businessNumber();
        info.representativeName = request.representativeName();
        info.businessType = request.businessType();
        info.businessAddress = request.businessAddress();
        info.privacyConsent = request.privacyConsent();
        info.privacyConsentAt = consentAt;
        return info;
    }
}
