package com.verby.indp.domain.store;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.store.dto.request.BusinessHour;
import com.verby.indp.domain.store.dto.request.GenreItem;
import com.verby.indp.domain.store.dto.request.TimePreference;
import com.verby.indp.domain.store.vo.PlaylistType;
import com.verby.indp.domain.store.vo.Tempo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Entity
@Getter
@Table(name = "store_music")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StoreMusic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_music_id")
    private Long storeMusicId;

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
    private Tempo musicTempo;

    @Column(name = "music_mood")
    private String musicMood;

    @OneToMany(mappedBy = "storeMusic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayMethod> playMethods = new ArrayList<>();

    @OneToMany(mappedBy = "storeMusic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicTimePreference> musicTimePreferences = new ArrayList<>();

    @OneToMany(mappedBy = "storeMusic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicGenre> genres = new ArrayList<>();

    public StoreMusic(String platform, String playedMusic, String rejectedSongNote,
        PlaylistType playlistType,
        Tempo musicTempo, String musicMood, List<PlayMethod.Method> playMethods,
        List<TimePreference> timePreferences, List<GenreItem> genreItems,
        List<BusinessHour> businessHours) {
        validatePlatform(platform);
        validatePlayedMusic(playedMusic);
        validatePlaylistType(playlistType);
        validateMusicTempo(musicTempo);
        validatePlayMethods(playMethods);
        this.platform = platform;
        this.playedMusic = playedMusic;
        this.rejectedSongNote = rejectedSongNote;
        this.playlistType = playlistType;
        this.musicTempo = musicTempo;
        this.musicMood = musicMood;
        this.playMethods = playMethods.stream()
            .map(playMethod -> new PlayMethod(this, playMethod))
            .toList();
        this.genres = genreItems.stream()
            .map(preferenceGenre -> new MusicGenre(this, preferenceGenre.genre(),
                preferenceGenre.preferenceType()))
            .collect(Collectors.toCollection(ArrayList::new));
        this.musicTimePreferences = buildMusicTimePreferences(playlistType, timePreferences,
            musicMood, businessHours);
    }

    public void updateGenres(List<GenreItem> genreItems) {
        this.genres.clear();
        genreItems.stream()
            .map(item -> new MusicGenre(this, item.genre(), item.preferenceType()))
            .forEach(this.genres::add);
    }

    public void updateTimePreferences(List<TimePreference> timePreferences) {
        this.musicTimePreferences.clear();
        timePreferences.stream()
            .map(tp -> new MusicTimePreference(this, tp.startTime().getHour(),
                tp.endTime().getHour(), tp.mood()))
            .forEach(this.musicTimePreferences::add);
    }

    private void validatePlatform(String platform) {
        if (platform == null || platform.isBlank()) {
            throw new BadRequestException("platform은 필수입니다.");
        }
    }

    private void validatePlayedMusic(String playedMusic) {
        if (playedMusic == null || playedMusic.isBlank()) {
            throw new BadRequestException("playedMusic은 필수입니다.");
        }
    }

    private void validatePlaylistType(PlaylistType playlistType) {
        if (playlistType == null) {
            throw new BadRequestException("playlistType은 필수입니다.");
        }
    }

    private void validateMusicTempo(Tempo musicTempo) {
        if (musicTempo == null) {
            throw new BadRequestException("musicTempo는 필수입니다.");
        }
    }

    private void validatePlayMethods(List<PlayMethod.Method> playMethods) {
        if (playMethods == null || playMethods.isEmpty()) {
            throw new BadRequestException("playMethods는 필수이며 비어있을 수 없습니다.");
        }
    }

    private List<MusicTimePreference> buildMusicTimePreferences(PlaylistType playlistType,
        List<TimePreference> timePreferences,
        String mood, List<BusinessHour> businessHours) {
        if (playlistType == PlaylistType.TIME_BASED) {
            return timePreferences.stream()
                .map(timePreference -> new MusicTimePreference(this,
                    timePreference.startTime().getHour(), timePreference.endTime().getHour(),
                    timePreference.mood()))
                .collect(Collectors.toCollection(ArrayList::new));
        }

        List<BusinessHour> openHours = businessHours.stream()
            .filter(bh -> !bh.isClosed())
            .toList();

        LocalTime openTime = openHours.stream().map(BusinessHour::openTime)
            .min(LocalTime::compareTo).orElse(LocalTime.MIN);
        LocalTime closeTime = openHours.stream().map(BusinessHour::closeTime)
            .max(LocalTime::compareTo).orElse(LocalTime.MAX);

        int openHour = openTime.getMinute() > 0 ? openTime.getHour() - 1 : openTime.getHour();
        int closeHour = closeTime.getMinute() > 0 ? closeTime.getHour() + 1 : closeTime.getHour();

        if (playlistType == PlaylistType.MUSIC_RECOMMENDED) {
            return IntStream.range(openHour, closeHour)
                .mapToObj(hour -> new MusicTimePreference(this, hour, hour + 1, null))
                .collect(Collectors.toCollection(ArrayList::new));

        } else if (playlistType == PlaylistType.CONSISTENT_MOOD) {
            return IntStream.range(openHour, closeHour)
                .mapToObj(hour -> new MusicTimePreference(this, hour, hour + 1, mood))
                .collect(Collectors.toCollection(ArrayList::new));
        }

        return new ArrayList<>();
    }
}
