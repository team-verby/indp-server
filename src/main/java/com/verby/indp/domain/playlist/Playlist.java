package com.verby.indp.domain.playlist;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "playlist")
public class Playlist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private Long playlistId;

    @Column(name = "is_playing")
    private boolean isPlaying = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_playlist_song_id")
    private PlaylistSong currentPlayingSong;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaylistSong> songs = new ArrayList<>();

    public void updateCurrentSong(PlaylistSong song) {
        this.currentPlayingSong = song;
        this.isPlaying = true;
    }

    public void stop() {
        this.isPlaying = false;
        this.currentPlayingSong = null;
    }
}
