package com.verby.indp.domain.playlist;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 음원 데이터 관리(무드별 곡 카탈로그)의 한 곡.
 * 매장과 무관한 전역 데이터로, 무드(mood)별로 묶여 랜덤 플레이리스트 생성의 재료가 된다.
 *
 * <p>position 은 무드 내 표시 순서(1~)를 의미한다.
 * artist/playTime/vid 는 비어있을 수 있다(엑셀 업로드 시 일부 열이 없을 수 있음).
 */
@Entity
@Getter
@Table(name = "music_catalog_song")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MusicCatalogSong extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "music_catalog_song_id")
    private Long musicCatalogSongId;

    @Column(name = "mood", nullable = false)
    private String mood;

    @Column(name = "position", nullable = false)
    private int position;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "artist")
    private String artist;

    @Column(name = "play_time")
    private String playTime;

    @Column(name = "vid")
    private String vid;

    public MusicCatalogSong(String mood, int position, String title, String artist,
        String playTime, String vid) {
        validateMood(mood);
        validatePosition(position);
        validateTitle(title);
        this.mood = mood;
        this.position = position;
        this.title = title;
        this.artist = artist;
        this.playTime = playTime;
        this.vid = vid;
    }

    private void validateMood(String mood) {
        if (mood == null || mood.isBlank()) {
            throw new BadRequestException("mood는 필수입니다.");
        }
    }

    private void validatePosition(int position) {
        if (position < 1) {
            throw new BadRequestException("position은 1 이상이어야 합니다.");
        }
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new BadRequestException("title은 필수입니다.");
        }
    }
}
