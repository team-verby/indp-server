package com.verby.indp.domain.playlist;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.recommendation.SongRecommendation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "playlist_song")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PlaylistSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_song_id")
    private Long playlistSongId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_recommendation_id")
    private SongRecommendation songRecommendation;

    @Column(name = "is_recommended")
    private boolean isRecommended = false;

    @Column(name = "vid")
    private String vid;

    @Column(name = "play_time")
    private Integer playTime;

    @Column(name = "title")
    private String title;

    @Column(name = "artist")
    private String artist;

    @Setter
    @Column(name = "play_order", nullable = false)
    private double playOrder;

    public PlaylistSong(SongRecommendation songRecommendation, boolean isRecommended, String vid, Integer playTime, String title, String artist, double playOrder) {
        validateVid(vid);
        validatePlayTime(playTime);
        validateTitle(title);
        validateArtist(artist);
        validatePlayOrder(playOrder);
        this.songRecommendation = songRecommendation;
        this.isRecommended = isRecommended;
        this.vid = vid;
        this.playTime = playTime;
        this.title = title;
        this.artist = artist;
        this.playOrder = playOrder;
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

    private void validatePlayOrder(double playOrder) {
        if (playOrder <= 0) {
            throw new BadRequestException("playOrder는 양수여야 합니다.");
        }
    }
}
