package com.verby.indp.domain.contact.service;

import static com.verby.indp.domain.contact.fixture.ContactFixture.contact;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.verby.indp.domain.common.event.MailSendEvent;
import com.verby.indp.domain.contact.Contact;
import com.verby.indp.domain.contact.dto.request.RegisterContactRequest;
import com.verby.indp.domain.contact.repository.ContactRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @InjectMocks
    private ContactService contactService;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Nested
    @DisplayName("registerContact 메소드 호출 시")
    class RegisterContact {

        @Test
        @DisplayName("성공: 문의 내용을 저장한다.")
        void registerContact() {
            // given
            Contact contact = contact();
            ReflectionTestUtils.setField(contact, "contactId", 1L);

            RegisterContactRequest request = new RegisterContactRequest(contact.getUserName(),
                contact.getContent(), contact.getPhoneNumber());

            when(contactRepository.save(any(Contact.class))).thenReturn(contact);

            // when
            contactService.registerContact(request);

            // then
            verify(contactRepository, times(1)).save(any(Contact.class));
            verify(applicationEventPublisher, times(1)).publishEvent(any(MailSendEvent.class));
        }

    }

}
