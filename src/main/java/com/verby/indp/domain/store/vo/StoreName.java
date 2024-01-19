package com.verby.indp.domain.store.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class StoreName {

    @Column(name = "name")
    private String name;

}
