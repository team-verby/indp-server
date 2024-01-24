package com.verby.indp.domain.song.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class SongFormName {

    @Column(name = "name")
    private String name;

    public SongFormName(String name) {
        this.name = name;
    }
}
