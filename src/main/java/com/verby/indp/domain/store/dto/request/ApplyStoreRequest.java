package com.verby.indp.domain.store.dto.request;

import com.verby.indp.domain.store.PlayMethod;
import com.verby.indp.domain.store.StoreMood;
import com.verby.indp.domain.store.StoreMusic;
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
    List<StoreMood.Vibe> moods,
    Integer lighting,
    StoreMusic.PlaylistType playlistType,
    List<TimePreference> timePreferences,
    String vibe,
    StoreMusic.Tempo tempo,
    List<String> rejectedGenres,
    String rejectedSongNote
) {
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
