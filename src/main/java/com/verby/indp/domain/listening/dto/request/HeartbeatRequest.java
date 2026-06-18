package com.verby.indp.domain.listening.dto.request;

/**
 * 청취 heartbeat 요청. 직전 비트 이후 누적된 실제 재생 델타(초)를 보낸다.
 * 비정상 값(null creatorId, 0 이하 seconds 등)은 서비스에서 조용히 무시한다.
 *
 * @param creatorId 청취 대상 크리에이터 ID
 * @param seconds   직전 전송 이후 추가 재생 초 (audio.currentTime 기반 델타)
 */
public record HeartbeatRequest(
    Long creatorId,
    int seconds
) {

}
