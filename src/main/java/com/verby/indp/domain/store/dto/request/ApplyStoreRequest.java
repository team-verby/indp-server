package com.verby.indp.domain.store.dto.request;

import com.verby.indp.domain.store.PlayMethod;
import com.verby.indp.domain.store.StoreMusic;
import com.verby.indp.domain.store.StoreVibe;

import java.util.List;

public record ApplyStoreRequest(
    String applicantName,
    String applicantPhone,
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
}
