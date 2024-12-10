package com.verby.indp.domain.region.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class RegionName {

    @Column(name = "name")
    private String name;

    public RegionName(String name) {
        this.name = name;
    }

}
