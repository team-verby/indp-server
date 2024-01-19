package com.verby.indp.domain.song.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class SongFormName {

    @Column(name = "name")
    private String name;

}
