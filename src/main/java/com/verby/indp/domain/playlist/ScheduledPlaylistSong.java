package com.verby.indp.domain.playlist;

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
    @JoinColumn(name = "scheduled_playlist_update_id", nullable = false)
    private ScheduledPlaylistUpdate scheduledPlaylistUpdate;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "artist", nullable = false)
    private String artist;

    @Column(name = "vid")
    private String vid;

    @Column(name = "play_time")
    private Integer playTime;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    public ScheduledPlaylistSong(String title, String artist, String vid, Integer playTime, int sortOrder) {
        this.title = title;
        this.artist = artist;
        this.vid = vid;
        this.playTime = playTime;
        this.sortOrder = sortOrder;
    }
}
