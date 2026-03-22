package com.verby.indp.domain.store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "store_music_time_preference")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StoreMusicTimePreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_music_time_preference_id")
    private Long storeMusicTimePreferenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "mood")
    private String mood;

    public StoreMusicTimePreference(Store store, LocalTime startTime, LocalTime endTime, String mood) {
        this.store = store;
        this.startTime = startTime;
        this.endTime = endTime;
        this.mood = mood;
    }
}
