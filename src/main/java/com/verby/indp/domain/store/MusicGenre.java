package com.verby.indp.domain.store;

import com.verby.indp.domain.store.vo.Genre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "music_genre")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MusicGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "music_genre_id")
    private Long musicGenreId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_music_id")
    private StoreMusic storeMusic;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Genre genre;

    @Enumerated(EnumType.STRING)
    @Column(name = "preference_type", nullable = false)
    private PreferenceType preferenceType;

    public MusicGenre(StoreMusic storeMusic, Genre genre, PreferenceType preferenceType) {
        this.storeMusic = storeMusic;
        this.genre = genre;
        this.preferenceType = preferenceType;
    }

    public boolean isPreferred() {
        return preferenceType == PreferenceType.LIKE;
    }

    public boolean isRejected() {
        return preferenceType == PreferenceType.DISLIKE;
    }

    public enum PreferenceType {
        LIKE,
        DISLIKE
    }
}
