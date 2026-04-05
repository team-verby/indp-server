package com.verby.indp.domain.store.dto.request;

import com.verby.indp.domain.store.PlayMethod;
import com.verby.indp.domain.store.StoreMusic;
import com.verby.indp.domain.store.StoreVibe;

import java.util.List;

public record UpdateStoreRequest(
    String name,
    String industry,
    String address,
    List<BusinessHour> businessHours,
    List<String> photoUrls,
    String customerAgeGroup,
    Integer lighting,
    String platform,
    String playedMusic,
    List<PlayMethod.Method> playMethods,
    List<StoreVibe.Vibe> vibes,
    StoreMusic.PlaylistType playlistType,
    List<TimePreference> timePreferences,
    StoreMusic.MusicTempo musicTempo,
    List<GenreItem> preferenceGenres,
    String rejectedSongNote,
    String mood
) {
}
