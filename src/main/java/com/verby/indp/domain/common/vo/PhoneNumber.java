package com.verby.indp.domain.common.vo;

import static java.util.Objects.isNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PhoneNumber {

    private static final String PHONE_NUMBER_PATTERN = "^[0-9]+$";
    private static final int MAX_PHONE_NUMBER_SIZE = 50;

    @Column(name = "phone_number")
    private String phoneNumber;

    public PhoneNumber(String phoneNumber) {
        validatePhoneNumber(phoneNumber);

        this.phoneNumber = phoneNumber;
    }

    private void validatePhoneNumber(String phoneNumber) {
        validateBlank(phoneNumber);
        validateSize(phoneNumber);
        validateValue(phoneNumber);
    }

    private void validateSize(String phoneNumber) {
        if (phoneNumber.length() > MAX_PHONE_NUMBER_SIZE) {
            throw new IllegalArgumentException("전화 번호의 크기는 최대 50자 입니다.");
        }
    }

    private void validateValue(String phoneNumber) {
        if (!phoneNumber.matches(PHONE_NUMBER_PATTERN)) {
            throw new IllegalArgumentException("전화 번호는 숫자만 입력 가능합니다.");
        }
    }

    private void validateBlank(String phoneNumber) {
        if (isNull(phoneNumber) || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("전화 번호를 입력해주세요.");
        }
    }
}
