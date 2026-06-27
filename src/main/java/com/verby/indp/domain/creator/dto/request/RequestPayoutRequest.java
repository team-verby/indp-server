package com.verby.indp.domain.creator.dto.request;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.settlement.TaxType;

/**
 * 정산(출금) 신청 요청. 신청 시점에 세금 처리 유형별 신원·계좌 정보와
 * 개인정보 수집·이용 동의를 함께 받는다.
 *
 * @param taxType            세금 유형 (INDIVIDUAL=개인 원천징수 / BUSINESS=사업자 세금계산서)
 * @param bankName           정산 입금 은행명 (공통, 필수)
 * @param accountNumber      정산 입금 계좌번호 (공통, 필수 / 저장 시 암호화)
 * @param accountHolder      예금주 (공통, 필수)
 * @param contact            연락처 (공통, 필수)
 * @param email              세금 관련 서류 수신 이메일 (선택)
 * @param residentName       성명 (개인, 필수)
 * @param residentNumber     주민등록번호 13자리 (개인, 필수 / 저장 시 암호화)
 * @param residentAddress    주소 (개인, 필수)
 * @param businessName       상호 (사업자, 필수)
 * @param businessNumber     사업자등록번호 10자리 (사업자, 필수)
 * @param representativeName 대표자명 (사업자, 필수)
 * @param businessType       업태/종목 (사업자, 선택)
 * @param businessAddress    사업장 주소 (사업자, 필수)
 * @param privacyConsent     개인정보 수집·이용 동의 여부 (필수, true)
 */
public record RequestPayoutRequest(
    TaxType taxType,
    String bankName,
    String accountNumber,
    String accountHolder,
    String contact,
    String email,
    String residentName,
    String residentNumber,
    String residentAddress,
    String businessName,
    String businessNumber,
    String representativeName,
    String businessType,
    String businessAddress,
    boolean privacyConsent
) {

    /** 신청 내용을 검증한다. 누락·형식 오류 시 {@link BadRequestException}를 던진다. */
    public void validate() {
        if (!privacyConsent) {
            throw new BadRequestException("개인정보 수집·이용에 동의해야 정산을 신청할 수 있습니다.");
        }
        if (taxType == null) {
            throw new BadRequestException("세금 처리 유형(개인/사업자)을 선택해주세요.");
        }
        requireText(bankName, "은행명");
        requireText(accountNumber, "계좌번호");
        requireText(accountHolder, "예금주");
        requireText(contact, "연락처");

        if (taxType == TaxType.INDIVIDUAL) {
            requireText(residentName, "성명");
            requireText(residentAddress, "주소");
            String rrn = digitsOnly(residentNumber);
            if (rrn.length() != 13) {
                throw new BadRequestException("주민등록번호 13자리를 정확히 입력해주세요.");
            }
        } else {
            requireText(businessName, "상호");
            requireText(representativeName, "대표자명");
            requireText(businessAddress, "사업장 주소");
            String bn = digitsOnly(businessNumber);
            if (bn.length() != 10) {
                throw new BadRequestException("사업자등록번호 10자리를 정확히 입력해주세요.");
            }
        }
    }

    private static void requireText(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException(label + "은(는) 필수입니다.");
        }
    }

    private static String digitsOnly(String value) {
        return value == null ? "" : value.replaceAll("[^0-9]", "");
    }
}
