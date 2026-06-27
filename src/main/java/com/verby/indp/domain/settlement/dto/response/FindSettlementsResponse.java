package com.verby.indp.domain.settlement.dto.response;

import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.settlement.SettlementRequest;
import com.verby.indp.domain.settlement.SettlementStatus;
import com.verby.indp.domain.settlement.SettlementTaxInfo;
import com.verby.indp.domain.settlement.TaxType;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 어드민 정산 신청 목록 응답.
 *
 * @param settlements 신청 목록 (최신 신청순)
 */
public record FindSettlementsResponse(
    List<SettlementItem> settlements
) {

    /**
     * @param settlementRequestId 정산 신청 ID
     * @param creatorId           크리에이터 ID
     * @param djName              DJ(채널)명
     * @param amount              신청 금액 (원)
     * @param status              상태 (REQUESTED/PAID/REJECTED)
     * @param requestedAt         신청 일시
     * @param processedAt         처리 일시 (미처리 시 null)
     * @param taxInfo             세금·신원·계좌 정보 요약 (민감정보 마스킹, 구버전 신청은 null)
     */
    public record SettlementItem(
        Long settlementRequestId,
        Long creatorId,
        String djName,
        long amount,
        SettlementStatus status,
        LocalDateTime requestedAt,
        LocalDateTime processedAt,
        TaxInfoSummary taxInfo
    ) {

        public static SettlementItem of(SettlementRequest request, Creator creator) {
            String djName = creator == null ? "(삭제된 크리에이터)" : creator.getDjName();
            return new SettlementItem(
                request.getSettlementRequestId(),
                request.getCreatorId(),
                djName,
                request.getAmount(),
                request.getStatus(),
                request.getRequestedAt(),
                request.getProcessedAt(),
                TaxInfoSummary.of(request.getTaxInfo())
            );
        }
    }

    /**
     * 정산 세금·신원·계좌 정보 요약. 주민등록번호·계좌번호는 마스킹해서 내려주며,
     * 전체 값은 {@code GET /api/admin/settlements/{id}/tax-info}로 복호화 조회한다.
     *
     * @param taxType              세금 유형 (INDIVIDUAL/BUSINESS)
     * @param displayName          표시 이름 (개인=성명 / 사업자=대표자명)
     * @param maskedResidentNumber 마스킹된 주민등록번호 (개인, 예: 901010-1******)
     * @param residentAddress      주소 (개인)
     * @param businessName         상호 (사업자)
     * @param businessNumber       사업자등록번호 (사업자)
     * @param representativeName   대표자명 (사업자)
     * @param businessType         업태/종목 (사업자)
     * @param businessAddress      사업장 주소 (사업자)
     * @param bankName             은행명
     * @param maskedAccountNumber  마스킹된 계좌번호
     * @param accountHolder        예금주
     * @param contact              연락처
     * @param email                세금 서류 수신 이메일
     * @param privacyConsentAt     개인정보 수집·이용 동의 일시
     */
    public record TaxInfoSummary(
        TaxType taxType,
        String displayName,
        String maskedResidentNumber,
        String residentAddress,
        String businessName,
        String businessNumber,
        String representativeName,
        String businessType,
        String businessAddress,
        String bankName,
        String maskedAccountNumber,
        String accountHolder,
        String contact,
        String email,
        LocalDateTime privacyConsentAt
    ) {

        public static TaxInfoSummary of(SettlementTaxInfo info) {
            if (info == null || info.getTaxType() == null) {
                return null;
            }
            String displayName = info.getTaxType() == TaxType.INDIVIDUAL
                ? info.getResidentName()
                : info.getRepresentativeName();
            return new TaxInfoSummary(
                info.getTaxType(),
                displayName,
                maskResidentNumber(info.getResidentNumber()),
                info.getResidentAddress(),
                info.getBusinessName(),
                info.getBusinessNumber(),
                info.getRepresentativeName(),
                info.getBusinessType(),
                info.getBusinessAddress(),
                info.getBankName(),
                maskAccountNumber(info.getAccountNumber()),
                info.getAccountHolder(),
                info.getContact(),
                info.getEmail(),
                info.getPrivacyConsentAt()
            );
        }

        /** 주민등록번호 → 앞 6자리 + 성별 1자리만 노출 (901010-1******). */
        private static String maskResidentNumber(String rrn) {
            if (rrn == null) {
                return null;
            }
            String digits = rrn.replaceAll("[^0-9]", "");
            if (digits.length() != 13) {
                return "******";
            }
            return digits.substring(0, 6) + "-" + digits.charAt(6) + "******";
        }

        /** 계좌번호 → 앞 3자리·뒤 2자리만 노출, 가운데 마스킹. */
        private static String maskAccountNumber(String account) {
            if (account == null) {
                return null;
            }
            String digits = account.replaceAll("[^0-9]", "");
            if (digits.length() <= 5) {
                return "*".repeat(Math.max(digits.length(), 1));
            }
            String head = digits.substring(0, 3);
            String tail = digits.substring(digits.length() - 2);
            return head + "*".repeat(digits.length() - 5) + tail;
        }
    }
}
