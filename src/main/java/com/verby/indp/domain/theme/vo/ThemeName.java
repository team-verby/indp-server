package com.verby.indp.domain.theme.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ThemeName {

    @Column(name = "name")
    private String name;

    public ThemeName(String name) {
        this.name = name;
    }
}
