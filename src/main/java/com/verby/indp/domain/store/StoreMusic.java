package com.verby.indp.domain.store;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "store_music")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StoreMusic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_music_id")
    private Long storeMusicId;

    @Setter
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

    @Enumerated(EnumType.STRING)
    @Column(name = "tempo")
    private MusicTempo musicTempo;

    @Column(name = "musicMood")
    private String musicMood;

    @OneToMany(mappedBy = "storeMusic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayMethod> playMethods = new ArrayList<>();

    @OneToMany(mappedBy = "storeMusic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicTimePreference> musicTimePreferences = new ArrayList<>();

    @OneToMany(mappedBy = "storeMusic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicGenre> genres = new ArrayList<>();

    public StoreMusic(String platform, String playedMusic, String rejectedSongNote, PlaylistType playlistType,
                      MusicTempo musicTempo, String musicMood, List<PlayMethod> playMethods,
                      List<MusicTimePreference> musicTimePreferences, List<MusicGenre> genres) {
        this.platform = platform;
        this.playedMusic = playedMusic;
        this.rejectedSongNote = rejectedSongNote;
        this.playlistType = playlistType;
        this.musicTempo = musicTempo;
        this.musicMood = musicMood;
        setPlayMethods(playMethods);
        setMusicTimePreferences(musicTimePreferences);
        setGenres(genres);
    }

    public enum PlaylistType {
        MUSIC_RECOMMENDED,
        TIME_BASED,
        CONSISTENT_VIBE
    }

    public enum MusicTempo {
        SLOW,
        CALM,
        NORMAL,
        LIVELY,
        UPBEAT
    }

    private void setPlayMethods(List<PlayMethod> playMethods) {
        this.playMethods = playMethods;
        playMethods.forEach(playMethod -> playMethod.setStoreMusic(this));
    }

    private void setMusicTimePreferences(List<MusicTimePreference> musicTimePreferences) {
        this.musicTimePreferences = musicTimePreferences;
        musicTimePreferences.forEach(musicTimePreference -> musicTimePreference.setStoreMusic(this));
    }

    private void setGenres(List<MusicGenre> genres) {
        this.genres = genres;
        genres.forEach(genre -> genre.setStoreMusic(this));
    }
}
