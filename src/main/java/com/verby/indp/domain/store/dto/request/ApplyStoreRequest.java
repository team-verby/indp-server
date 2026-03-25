package com.verby.indp.domain.store.dto.request;

import com.verby.indp.domain.store.MusicGenre;
import com.verby.indp.domain.store.PlayMethod;
import com.verby.indp.domain.store.StoreMusic;
import com.verby.indp.domain.store.StoreVibe;
import java.time.LocalTime;
import java.util.List;

public record ApplyStoreRequest(
    String applicantName,
    String applicantPhone,
    String inquiryContent,
    Long planId,
    Integer usagePeriod,
    String name,
    String industry,
    String address,
    List<BusinessHour> businessHours,
    List<String> photoUrls,
    String platform,
    String playedMusic,
    String customerAgeGroup,
    List<PlayMethod.Method> playMethods,
    List<StoreVibe.Vibe> vibes,
    Integer lighting,
    StoreMusic.PlaylistType playlistType,
    List<TimePreference> timePreferences,
    StoreMusic.MusicTempo musicTempo,
    List<GenreItem> preferenceGenres,
    String rejectedSongNote,
    String mood
) {
    public record GenreItem(MusicGenre.Genre genre, MusicGenre.PreferenceType preferenceType) {

    }

    public record BusinessHour(
        int dayOfWeek,
        LocalTime openTime,
        LocalTime closeTime,
        boolean isClosed
    ) {
    }

    public record TimePreference(
        LocalTime startTime,
        LocalTime endTime,
        String mood
    ) {
    }
}
