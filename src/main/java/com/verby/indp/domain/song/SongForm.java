package com.verby.indp.domain.song;

import com.verby.indp.domain.song.vo.SongFormName;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "song_form")
public class SongForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_form_id")
    private long songFormId;

    @Embedded
    private SongFormName name;

}
