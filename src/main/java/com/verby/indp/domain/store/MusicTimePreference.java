package com.verby.indp.domain.store;

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

    public MusicTimePreference(int startTimeHour, int endTimeHour, String mood) {
        this.startTimeHour = startTimeHour;
        this.endTimeHour = endTimeHour;
        this.mood = mood;
    }
}
