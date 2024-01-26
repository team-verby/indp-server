package com.verby.indp.acceptance;

import static com.verby.indp.domain.contact.fixture.ContactFixture.contact;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.verby.indp.acceptance.support.ContactSupporter;
import com.verby.indp.domain.common.notification.mail.Mail;
import com.verby.indp.domain.common.notification.mail.MailService;
import com.verby.indp.domain.contact.Contact;
import com.verby.indp.domain.contact.dto.request.RegisterContactRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("contact 인수 테스트")
class ContactAcceptanceTest extends BaseAcceptanceTest {

    @MockBean
    private MailService mailService;

    @Test
    @DisplayName("문의 내용을 등록한다.")
    void registerContact() {
        // given
        Contact contact = contact();
        RegisterContactRequest request = new RegisterContactRequest(contact.getUserName(),
            contact.getContent(), contact.getPhoneNumber());

        // when
        ExtractableResponse<Response> result = ContactSupporter.registerContact(request);

        // then
        assertThat(result.statusCode()).isEqualTo(201);
        assertThat(result.header("Location")).isNotBlank();
        verify(mailService, times(1)).sendMail(any(Mail.class));
    }

}
