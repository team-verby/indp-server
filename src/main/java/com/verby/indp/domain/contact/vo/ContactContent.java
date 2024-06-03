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
public class ContactContent {

    private static final int MAX_CONTENT_SIZE = 150;

    @Column(name = "content")
    private String content;

    public ContactContent(String content) {
        validateContent(content);

        this.content = content;
    }

    private void validateContent(String content) {
        validateBlank(content);
        validateSize(content);
    }

    private void validateBlank(String content) {
        if (Objects.isNull(content) || content.isBlank()) {
            throw new BadRequestException("문의 내용을 입력해주세요.");
        }
    }

    private void validateSize(String content) {
        if (content.length() > MAX_CONTENT_SIZE) {
            throw new BadRequestException(
                MessageFormat.format("문의 내용은 최대 {0}자 입니다.", MAX_CONTENT_SIZE));
        }
    }
}
