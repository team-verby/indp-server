package com.verby.indp.domain.playlist;

import com.verby.indp.domain.recommendation.SongRecommendation;
import jakarta.persistence.CascadeType;
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

@Entity
@Getter
@Table(name = "playlist_song")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PlaylistSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_song_id")
    private Long playlistSongId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "song_recommendation_id")
    private SongRecommendation songRecommendation;

    @Column(name = "is_recommended")
    private boolean isRecommended;

    @Column(name = "vid")
    private String vid;

    @Column(name = "play_time")
    private Integer playTime;

    @Column(name = "title")
    private String title;

    @Column(name = "artist")
    private String artist;

    @Column(name = "play_order", nullable = false)
    private double playOrder;

    public void updatePosition(double position) {
        this.playOrder = position;
    }

    public PlaylistSong(Playlist playlist, SongRecommendation songRecommendation, boolean isRecommended, String vid, Integer playTime, String title, String artist, double playOrder) {
        this.playlist = playlist;
        this.songRecommendation = songRecommendation;
        this.isRecommended = isRecommended;
        this.vid = vid;
        this.playTime = playTime;
        this.title = title;
        this.artist = artist;
        this.playOrder = playOrder;
    }
}
