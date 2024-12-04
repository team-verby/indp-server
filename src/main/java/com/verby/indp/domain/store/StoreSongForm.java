package com.verby.indp.domain.store;

import com.verby.indp.domain.song.SongForm;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store_song_form")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StoreSongForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_song_form_id")
    private long storeSongFormId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_form_id")
    private SongForm songForm;

    public StoreSongForm(Store store, SongForm songForm) {
        this.store = store;
        this.songForm = songForm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StoreSongForm that = (StoreSongForm) o;
        return Objects.equals(store, that.store) && Objects.equals(songForm,
            that.songForm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store, songForm);
    }
}
