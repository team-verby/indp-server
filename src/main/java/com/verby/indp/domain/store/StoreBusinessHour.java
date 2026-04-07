package com.verby.indp.domain.store;

import com.verby.indp.domain.common.exception.BadRequestException;
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
        validateStore(store);
        validateDayOfWeek(dayOfWeek);
        validateOpenHour(isClosed, openTime, closeTime);
        this.store = store;
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isClosed = isClosed;
    }

    private void validateStore(Store store) {
        if (store == null) {
            throw new BadRequestException("store는 필수입니다.");
        }
    }

    private void validateDayOfWeek(int dayOfWeek) {
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            throw new BadRequestException("dayOfWeek는 1~7 사이여야 합니다.");
        }
    }

    private void validateOpenHour(boolean isClosed, LocalTime openTime, LocalTime closeTime) {
        if (isClosed) {
            return;
        }
        if (openTime == null) {
            throw new BadRequestException("openTime은 필수입니다.");
        }
        if (closeTime == null) {
            throw new BadRequestException("closeTime은 필수입니다.");
        }
        if (!openTime.isBefore(closeTime)) {
            throw new BadRequestException("openTime은 closeTime보다 빨라야 합니다.");
        }
    }
}
