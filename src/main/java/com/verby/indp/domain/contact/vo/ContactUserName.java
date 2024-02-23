package com.verby.indp.domain.contact.vo;

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
public class ContactUserName {

    private static final int MAX_USER_NAME_SIZE = 50;

    @Column(name = "user_name")
    private String userName;

    public ContactUserName(String userName) {
        validateUserName(userName);

        this.userName = userName;
    }

    private void validateUserName(String userName) {
        validateBlank(userName);
        validateSize(userName);
    }

    private void validateBlank(String userName) {
        if (Objects.isNull(userName) || userName.isBlank()) {
            throw new BadRequestException("문의자 성함을 입력해주세요.");
        }
    }

    private void validateSize(String userName) {
        if (userName.length() > MAX_USER_NAME_SIZE) {
            throw new BadRequestException(
                MessageFormat.format("문의자 성함은 최대 {0}자 입니다.", MAX_USER_NAME_SIZE));
        }
    }
}
