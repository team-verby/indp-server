package com.verby.indp.domain.common.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Address {

    @Column(name = "address")
    private String address;

}
