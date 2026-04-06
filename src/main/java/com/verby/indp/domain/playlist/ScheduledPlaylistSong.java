package com.verby.indp.domain.playlist;

import com.verby.indp.domain.common.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "scheduled_playlist_song")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ScheduledPlaylistSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduled_playlist_song_id")
    private Long scheduledPlaylistSongId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduled_playlist_id", nullable = false)
    private ScheduledPlaylist scheduledPlaylist;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "artist", nullable = false)
    private String artist;

    @Column(name = "vid")
    private String vid;

    @Column(name = "play_time")
    private Integer playTime;

    @Column(name = "play_order", nullable = false)
    private double playOrder;

    public ScheduledPlaylistSong(String title, String artist, String vid, Integer playTime, Double playOrder) {
        validateVid(vid);
        validatePlayTime(playTime);
        validateTitle(title);
        validateArtist(artist);
        validatePlayOrder(playOrder);
        this.title = title;
        this.artist = artist;
        this.vid = vid;
        this.playTime = playTime;
        this.playOrder = playOrder;
    }

    private void validateVid(String vid) {
        if (vid == null || vid.isBlank()) throw new BadRequestException("vid는 필수입니다.");
    }

    private void validatePlayTime(Integer playTime) {
        if (playTime == null || playTime <= 0) throw new BadRequestException("playTime은 양수여야 합니다.");
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) throw new BadRequestException("title은 필수입니다.");
    }

    private void validateArtist(String artist) {
        if (artist == null || artist.isBlank()) throw new BadRequestException("artist는 필수입니다.");
    }

    private void validatePlayOrder(Double playOrder) {
        if (playOrder == null || playOrder <= 0) throw new BadRequestException("playOrder는 양수여야 합니다.");
    }
}
