package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreMusic;
import com.verby.indp.domain.store.StoreMusicTimePreference;
import java.time.LocalTime;
import java.util.List;

public record FindOwnerStoreResponse(
    ApplyInfo applyInfo,
    StoreInfo storeInfo,
    MusicInfo musicInfo
) {

    public static FindOwnerStoreResponse from(Store store) {
        ApplyInfo applyInfo = store.getStoreApply() != null
            ? new ApplyInfo(
                store.getStoreApply().getApplicantName(),
                store.getStoreApply().getApplicantPhone())
            : null;

        List<BusinessHourItem> hours = store.getBusinessHours().stream()
            .map(h -> new BusinessHourItem(h.getDayOfWeek(), h.getOpenTime(), h.getCloseTime(), h.isClosed()))
            .toList();

        List<String> photoUrls = store.getPhotos().stream()
            .map(p -> p.getImageUrl())
            .toList();

        StoreInfo storeInfo = new StoreInfo(
            store.getName(), store.getIndustry(), store.getAddress(), hours, photoUrls);

        MusicInfo musicInfo = null;
        if (store.getStoreMusic() != null) {
            StoreMusic m = store.getStoreMusic();
            List<String> moods = store.getMoods().stream().map(sm -> sm.getVibe().name()).toList();
            List<String> methods = store.getPlayMethods().stream().map(pm -> pm.getMethod().name()).toList();
            List<String> genres = store.getGenres().stream().map(g -> g.getGenre()).toList();

            List<TimePreferenceItem> timePreferences = store.getMusicTimePreferences().stream()
                .map(TimePreferenceItem::from)
                .toList();

            musicInfo = new MusicInfo(
                store.getCustomerAgeGroup(), methods, moods, store.getLighting(),
                m.getPlaylistType() != null ? m.getPlaylistType().name() : null,
                timePreferences,
                m.getVibe(),
                m.getTempo() != null ? m.getTempo().name() : null,
                genres, m.getRejectedSongNote());
        }

        return new FindOwnerStoreResponse(applyInfo, storeInfo, musicInfo);
    }

    public record ApplyInfo(String applicantName, String applicantPhone) {
    }

    public record StoreInfo(
        String name,
        String industry,
        String address,
        List<BusinessHourItem> businessHours,
        List<String> photoUrls
    ) {
    }

    public record BusinessHourItem(int dayOfWeek, LocalTime openTime, LocalTime closeTime, boolean isClosed) {
    }

    public record TimePreferenceItem(LocalTime startTime, LocalTime endTime, String mood) {
        public static TimePreferenceItem from(StoreMusicTimePreference tp) {
            return new TimePreferenceItem(tp.getStartTime(), tp.getEndTime(), tp.getMood());
        }
    }

    public record MusicInfo(
        String customerAgeGroup,
        List<String> playMethods,
        List<String> moods,
        Integer lighting,
        String playlistType,
        List<TimePreferenceItem> timePreferences,
        String vibe,
        String tempo,
        List<String> rejectedGenres,
        String rejectedSongNote
    ) {
    }
}
