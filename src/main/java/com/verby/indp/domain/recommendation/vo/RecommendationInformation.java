package com.verby.indp.domain.recommendation.vo;

import com.verby.indp.domain.common.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.text.MessageFormat;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class RecommendationInformation {

    private static final int MAX_INFORMATION_SIZE = 50;

    @Column(name = "information")
    private String information;

    public RecommendationInformation(String information) {
        validateInformation(information);

        this.information = information;
    }

    private void validateInformation(String information) {
        validateBlank(information);
        validateSize(information);
    }

    private void validateBlank(String information) {
        if (Objects.isNull(information) || information.isBlank()) {
            throw new BadRequestException("추천 음악 정보를 입력해주세요.");
        }
    }

    private void validateSize(String information) {
        if (information.length() > MAX_INFORMATION_SIZE) {
            throw new BadRequestException(
                MessageFormat.format("추천 음악 정보의 크기는 최대 {0}자 입니다.", MAX_INFORMATION_SIZE));
        }
    }
}
