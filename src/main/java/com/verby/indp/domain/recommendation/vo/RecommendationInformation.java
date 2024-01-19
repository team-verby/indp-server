package com.verby.indp.domain.recommendation.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class RecommendationInformation {

    @Column(name = "information")
    private String information;

}
