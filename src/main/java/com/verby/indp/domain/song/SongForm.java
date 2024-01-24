package com.verby.indp.domain.song;

import com.verby.indp.domain.song.vo.SongFormName;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "song_form")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class SongForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_form_id")
    private long songFormId;

    @Embedded
    private SongFormName name;

    public SongForm(String name) {
        this.name = new SongFormName(name);
    }

    public String getName() {
        return name.getName();
    }
}
