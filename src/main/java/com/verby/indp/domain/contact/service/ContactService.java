package com.verby.indp.domain.contact.service;

import com.verby.indp.domain.contact.event.ContactMailEvent;
import com.verby.indp.domain.contact.Contact;
import com.verby.indp.domain.contact.dto.request.RegisterContactRequest;
import com.verby.indp.domain.contact.repository.ContactRepository;
import com.verby.indp.domain.notification.dto.ContactMail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactService {

    @Value("${spring.mail.username}")
    private String to;

    private final ContactRepository contactRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public long registerContact(RegisterContactRequest request) {
        Contact contact = new Contact(request.userName(), request.content(), request.phoneNumber());
        Contact persistContact = contactRepository.save(contact);

        sendMail(request);

        return persistContact.getContactId();
    }

    private void sendMail(RegisterContactRequest request) {
        ContactMail contactMailRequest = ContactMail.of(to, request.content(),
            request.userName(), request.phoneNumber());
        applicationEventPublisher.publishEvent(new ContactMailEvent(contactMailRequest));
    }

}
