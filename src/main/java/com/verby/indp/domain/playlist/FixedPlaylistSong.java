package com.verby.indp.domain.playlist;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.store.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 매장 플레이리스트에 반복적으로 들어가야 하는 "특정곡".
 * 지정한 기간([startDate, endDate]) 동안, 해당 시간대(targetHour)의 정해진 순서(position)에 삽입된다.
 * 한 번 저장해두면 매일 플레이리스트 생성 시 클릭만으로 다시 입력할 필요 없이 적용할 수 있다.
 */
@Entity
@Getter
@Table(name = "fixed_playlist_song")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class FixedPlaylistSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fixed_playlist_song_id")
    private Long fixedPlaylistSongId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "target_hour", nullable = false)
    private int targetHour;

    @Column(name = "position", nullable = false)
    private int position;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "artist", nullable = false)
    private String artist;

    @Column(name = "vid", nullable = false)
    private String vid;

    @Column(name = "play_time", nullable = false)
    private Integer playTime;

    public FixedPlaylistSong(Store store, LocalDate startDate, LocalDate endDate,
        Integer targetHour, Integer position, String title, String artist, String vid,
        Integer playTime) {
        validateStore(store);
        validatePeriod(startDate, endDate);
        validateTargetHour(targetHour);
        validatePosition(position);
        validateTitle(title);
        validateArtist(artist);
        validateVid(vid);
        validatePlayTime(playTime);
        this.store = store;
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetHour = targetHour;
        this.position = position;
        this.title = title;
        this.artist = artist;
        this.vid = vid;
        this.playTime = playTime;
    }

    private void validateStore(Store store) {
        if (store == null) {
            throw new BadRequestException("store는 필수입니다.");
        }
    }

    private void validatePeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new BadRequestException("startDate와 endDate는 필수입니다.");
        }
        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("endDate는 startDate보다 빠를 수 없습니다.");
        }
    }

    private void validateTargetHour(Integer targetHour) {
        if (targetHour == null || targetHour < 0 || targetHour > 23) {
            throw new BadRequestException("targetHour는 0~23 사이여야 합니다.");
        }
    }

    private void validatePosition(Integer position) {
        if (position == null || position < 1 || position > 18) {
            throw new BadRequestException("position은 1~18 사이여야 합니다.");
        }
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new BadRequestException("title은 필수입니다.");
        }
    }

    private void validateArtist(String artist) {
        if (artist == null || artist.isBlank()) {
            throw new BadRequestException("artist는 필수입니다.");
        }
    }

    private void validateVid(String vid) {
        if (vid == null || vid.isBlank()) {
            throw new BadRequestException("vid는 필수입니다.");
        }
    }

    private void validatePlayTime(Integer playTime) {
        if (playTime == null || playTime <= 0) {
            throw new BadRequestException("playTime은 양수여야 합니다.");
        }
    }
}
