package com.verby.indp.domain.playlist;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 매장별 "시간대별 무드 매핑"의 한 칸(매장 × 시간대 → 무드).
 * 랜덤 플레이리스트 생성 시, 매장의 각 영업 시간대에 어떤 무드의 곡을 틀지 결정하는 재료다.
 *
 * <p>store_name 은 매장 이름, hour 는 0~23 시간대, mood 는 음원 카탈로그({@link MusicCatalogSong})의
 * 무드명과 매칭된다. 기존에는 관리자 브라우저 localStorage 에만 저장돼 기기/브라우저가 바뀌면
 * 사라졌으나, 서버에 영속화해 어느 환경에서 접속해도 유지되도록 한다.
 */
@Entity
@Getter
@Table(name = "store_mood_schedule")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StoreMoodSchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_mood_schedule_id")
    private Long storeMoodScheduleId;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "hour", nullable = false)
    private int hour;

    @Column(name = "mood", nullable = false)
    private String mood;

    public StoreMoodSchedule(String storeName, int hour, String mood) {
        validateStoreName(storeName);
        validateHour(hour);
        validateMood(mood);
        this.storeName = storeName;
        this.hour = hour;
        this.mood = mood;
    }

    private void validateStoreName(String storeName) {
        if (storeName == null || storeName.isBlank()) {
            throw new BadRequestException("storeName은 필수입니다.");
        }
    }

    private void validateHour(int hour) {
        if (hour < 0 || hour > 23) {
            throw new BadRequestException("hour는 0~23 사이여야 합니다.");
        }
    }

    private void validateMood(String mood) {
        if (mood == null || mood.isBlank()) {
            throw new BadRequestException("mood는 필수입니다.");
        }
    }
}
