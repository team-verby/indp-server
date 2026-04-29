package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.store.*;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.SubscriptionStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public record FindStoreByAdminResponse(
    ApplyInfo applyInfo,
    StoreInfo storeInfo,
    MusicInfo musicInfo,
    List<SubscriptionInfo> subscriptions
) {

    private static final Set<SubscriptionStatus> ACTIVE_STATUSES = Set.of(
        SubscriptionStatus.ACTIVE, SubscriptionStatus.PENDING_ACTIVE
    );

    public static FindStoreByAdminResponse from(Store store) {
        StoreApply storeApply = store.getStoreApply();

        ApplyInfo applyInfo = ApplyInfo.from(storeApply);
        StoreInfo storeInfo = StoreInfo.from(store);
        MusicInfo musicInfo = MusicInfo.from(store.getStoreMusic());
        List<SubscriptionInfo> subscriptions = store.getSubscriptions().stream()
            .filter(s -> ACTIVE_STATUSES.contains(s.getStatus()))
            .map(SubscriptionInfo::from)
            .toList();

        return new FindStoreByAdminResponse(applyInfo, storeInfo, musicInfo, subscriptions);
    }

    private record ApplyInfo(String applicantName, String applicantPhone) {

        private static ApplyInfo from(StoreApply storeApply) {
            return new ApplyInfo(storeApply.getApplicantName(), storeApply.getApplicantPhone());
        }
    }

    private record StoreInfo(
        String name,
        String industry,
        String address,
        List<BusinessHourItem> businessHours,
        String customerAgeGroup,
        List<String> photoUrls,
        List<String> vibes,
        int lighting
    ) {

        private static StoreInfo from(Store store) {
            List<BusinessHourItem> businessHourItems = store.getBusinessHours().stream()
                .map(BusinessHourItem::from)
                .toList();
            List<String> photoUrls = store.getPhotos().stream()
                .map(StorePhoto::getImageUrl)
                .toList();
            List<String> vibes = store.getVibes().stream()
                .map(storeVibe -> storeVibe.getVibe().name())
                .toList();
            return new StoreInfo(store.getName(), store.getIndustry(), store.getAddress(),
                businessHourItems, store.getCustomerAgeGroup(), photoUrls, vibes,
                store.getLighting());
        }
    }

    private record BusinessHourItem(int dayOfWeek, LocalTime openTime, LocalTime closeTime,
                                    boolean isClosed) {

        private static BusinessHourItem from(StoreBusinessHour businessHour) {
            return new BusinessHourItem(businessHour.getDayOfWeek(), businessHour.getOpenTime(),
                businessHour.getCloseTime(), businessHour.isClosed());
        }
    }

    private record MusicInfo(
        List<String> playMethods,
        String playlistType,
        List<TimePreferenceItem> timePreferences,
        String MusicPlatform,
        String playedMusic,
        String musicMood,
        String musicTempo,
        List<String> rejectedGenres,
        String rejectedSongNote
    ) {

        private static MusicInfo from(StoreMusic storeMusic) {
            List<String> methods = storeMusic.getPlayMethods().stream()
                .map(pm -> pm.getMethod().name()).toList();
            List<TimePreferenceItem> timePreferences = storeMusic.getMusicTimePreferences().stream()
                .map(TimePreferenceItem::from)
                .toList();
            List<String> rejectedGenres = storeMusic.getGenres().stream()
                .filter(MusicGenre::isRejected)
                .map(genre -> genre.getGenre().name()).toList();

            return new MusicInfo(methods, storeMusic.getPlaylistType().name(), timePreferences,
                storeMusic.getPlatform(),
                storeMusic.getPlayedMusic(), storeMusic.getMusicMood(),
                storeMusic.getMusicTempo().name(), rejectedGenres, storeMusic.getRejectedSongNote());
        }
    }

    private record TimePreferenceItem(int startTimeHour, int endTimeHour, String mood) {

        private static TimePreferenceItem from(MusicTimePreference timePreference) {
            return new TimePreferenceItem(timePreference.getStartTimeHour(),
                timePreference.getEndTimeHour(), timePreference.getMood());
        }
    }

    private record SubscriptionInfo(String planType, LocalDate startDate, LocalDate endDate,
                                    String status) {

        private static SubscriptionInfo from(StoreSubscription subscription) {
            return new SubscriptionInfo(subscription.getPlan().getType().name(),
                subscription.getStartDate(),
                subscription.getEndDate(), subscription.getStatus().name());
        }
    }
}

