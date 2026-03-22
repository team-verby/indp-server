package com.verby.indp.domain.store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "store_mood")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StoreMood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_mood_id")
    private Long storeMoodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(name = "vibe")
    private Vibe vibe;

    public StoreMood(Store store, Vibe vibe) {
        this.store = store;
        this.vibe = vibe;
    }

    public enum Vibe {
        CALM,
        MODERN,
        ELEGANT,
        DARK,
        NATURAL,
        OTHER
    }
}
