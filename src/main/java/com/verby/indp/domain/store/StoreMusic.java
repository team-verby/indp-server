package com.verby.indp.domain.store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "store_music")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StoreMusic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_music_id")
    private Long storeMusicId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "platform")
    private String platform;

    @Column(name = "played_music", columnDefinition = "TEXT")
    private String playedMusic;

    @Column(name = "rejected_song_note", columnDefinition = "TEXT")
    private String rejectedSongNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "playlist_type")
    private PlaylistType playlistType;

    @Column(name = "vibe")
    private String vibe;

    @Enumerated(EnumType.STRING)
    @Column(name = "tempo")
    private Tempo tempo;

    public StoreMusic(Store store, String platform, String playedMusic, PlaylistType playlistType, String vibe, Tempo tempo, String rejectedSongNote) {
        this.store = store;
        this.platform = platform;
        this.playedMusic = playedMusic;
        this.playlistType = playlistType;
        this.vibe = vibe;
        this.tempo = tempo;
        this.rejectedSongNote = rejectedSongNote;
    }

    public enum PlaylistType {
        MUSIC_RECOMMENDED,
        TIME_BASED,
        CONSISTENT_VIBE
    }

    public enum Tempo {
        SLOW,
        CALM,
        NORMAL,
        LIVELY,
        UPBEAT
    }
}
