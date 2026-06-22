package com.verby.indp.domain.creator.dto.request;

/**
 * DJ 크리에이터 지원 신청. 메인 페이지 모집 배너에서 비로그인 상태로 제출한다.
 * 수집 항목은 검토·연락 목적의 최소 개인정보와 유튜브 플레이리스트 URL이며,
 * 개인정보 수집·이용 동의(privacyConsent)가 true일 때만 접수된다.
 *
 * @param name           지원자 성명
 * @param phone          연락처(휴대폰)
 * @param email          회신용 이메일
 * @param youtubeUrl     유튜브 플레이리스트 URL
 * @param introduction   간단 소개 (선택)
 * @param privacyConsent 개인정보 수집·이용 동의 여부
 */
public record CreatorApplicationRequest(
    String name,
    String phone,
    String email,
    String youtubeUrl,
    String introduction,
    boolean privacyConsent
) {

}
