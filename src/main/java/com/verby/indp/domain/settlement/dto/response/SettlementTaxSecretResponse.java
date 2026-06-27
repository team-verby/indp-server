package com.verby.indp.domain.settlement.dto.response;

import com.verby.indp.domain.settlement.SettlementTaxInfo;

/**
 * 정산 처리 시 어드민이 "전체 보기"로 복호화 조회하는 민감 개인정보(전체 값).
 * 평소 목록에는 마스킹 값만 내려가고, 이 응답으로만 전체 값을 노출한다.
 *
 * @param residentNumber 주민등록번호 전체 (개인, 없으면 null)
 * @param accountNumber  계좌번호 전체
 */
public record SettlementTaxSecretResponse(
    String residentNumber,
    String accountNumber
) {

    public static SettlementTaxSecretResponse of(SettlementTaxInfo info) {
        if (info == null) {
            return new SettlementTaxSecretResponse(null, null);
        }
        return new SettlementTaxSecretResponse(info.getResidentNumber(), info.getAccountNumber());
    }
}
