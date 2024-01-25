package com.verby.indp.domain.contact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ContactTest {

    @Nested
    @DisplayName("Contact 생성 시")
    class NewContact {

        @Test
        @DisplayName("성공: Contact 를 생성한다.")
        void newContact() {
            // given
            String userName = "버비";
            String content = "버비버비버비";
            String phoneNumber = "01012345678";

            // when
            Exception exception = catchException(() -> new Contact(userName, content, phoneNumber));

            // then
            assertThat(exception).isNull();
        }

    }

}
