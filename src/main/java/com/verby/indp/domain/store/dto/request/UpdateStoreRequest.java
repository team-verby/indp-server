package com.verby.indp.domain.store.dto.request;

import com.verby.indp.domain.store.PlayMethod;
import com.verby.indp.domain.store.vo.PlaylistType;
import com.verby.indp.domain.store.vo.Tempo;
import com.verby.indp.domain.store.vo.Vibe;

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
    List<Vibe> vibes,
    PlaylistType playlistType,
    List<TimePreference> timePreferences,
    Tempo musicTempo,
    List<GenreItem> preferenceGenres,
    String rejectedSongNote,
    String mood
) {

}
