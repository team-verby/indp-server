package com.verby.indp.domain.store;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Table(name = "store_business_hour")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StoreBusinessHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_business_hour_id")
    private Long storeBusinessHourId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "day_of_week")
    private int dayOfWeek;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(name = "is_closed")
    private boolean isClosed;

    public StoreBusinessHour(Store store, int dayOfWeek, LocalTime openTime, LocalTime closeTime, boolean isClosed) {
        this.store = store;
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isClosed = isClosed;
    }
}
