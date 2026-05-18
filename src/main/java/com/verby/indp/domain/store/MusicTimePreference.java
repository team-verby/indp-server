package com.verby.indp.domain.store;

import com.verby.indp.domain.common.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "store_music_time_preference")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MusicTimePreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_music_time_preference_id")
    private Long storeMusicTimePreferenceId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_music_id")
    private StoreMusic storeMusic;

    @Column(name = "start_time_hour")
    private int startTimeHour;

    @Column(name = "end_time_hour")
    private int endTimeHour;

    @Column(name = "mood")
    private String mood;

    public MusicTimePreference(StoreMusic storeMusic, int startTimeHour, int endTimeHour,
        String mood) {
        validateStoreMusic(storeMusic);
        validateStartTimeHour(startTimeHour);
        validateEndTimeHour(endTimeHour);
        validateTimeRange(startTimeHour, endTimeHour);
        this.storeMusic = storeMusic;
        this.startTimeHour = startTimeHour;
        this.endTimeHour = endTimeHour;
        this.mood = mood;
    }

    private void validateStoreMusic(StoreMusic storeMusic) {
        if (storeMusic == null) {
            throw new BadRequestException("storeMusic은 필수입니다.");
        }
    }

    private void validateStartTimeHour(int startTimeHour) {
        if (startTimeHour < 0 || startTimeHour > 23) {
            throw new BadRequestException("startTimeHour는 0~23 사이여야 합니다.");
        }
    }

    private void validateEndTimeHour(int endTimeHour) {
        if (endTimeHour < 0 || endTimeHour > 23) {
            throw new BadRequestException("endTimeHour는 0~23 사이여야 합니다.");
        }
    }

    private void validateTimeRange(int startTimeHour, int endTimeHour) {
        boolean isMidnightWrap = startTimeHour == 23 && endTimeHour == 0;
        if (!isMidnightWrap && startTimeHour >= endTimeHour) {
            throw new BadRequestException("startTimeHour는 endTimeHour보다 작아야 합니다.");
        }
    }
}
