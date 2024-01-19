package com.verby.indp.domain.store.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class StoreRegion {

    @Column(name = "region")
    private String region;

}
