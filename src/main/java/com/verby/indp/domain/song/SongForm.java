package com.verby.indp.domain.song;

import com.verby.indp.domain.song.vo.SongFormName;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "song_form")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class SongForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_form_id")
    private Long songFormId;

    @Embedded
    private SongFormName name;

    public SongForm(String name) {
        this.name = new SongFormName(name);
    }

    public String getName() {
        return name.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SongForm songForm = (SongForm) o;
        return Objects.equals(songFormId, songForm.songFormId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(songFormId);
    }
}
