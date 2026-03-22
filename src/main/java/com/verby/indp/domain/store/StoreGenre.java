package com.verby.indp.domain.store;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "store_genre")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StoreGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_genre_id")
    private Long storeGenreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "genre")
    private String genre;

    @Enumerated(EnumType.STRING)
    @Column(name = "preference_type", nullable = false)
    private PreferenceType preferenceType;

    public StoreGenre(Store store, String genre) {
        this.store = store;
        this.genre = genre;
    }

    private enum PreferenceType {
        LIKE,
        DISLIKE
    }
}
