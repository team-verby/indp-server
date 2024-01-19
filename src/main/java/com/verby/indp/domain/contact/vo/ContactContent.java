package com.verby.indp.domain.contact.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ContactContent {

    @Column(name = "content")
    private String content;

}
