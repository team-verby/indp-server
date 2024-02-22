package com.verby.indp.domain.contact.service;

import com.verby.indp.domain.common.event.MailSendEvent;
import com.verby.indp.domain.notification.dto.Mail;
import com.verby.indp.domain.contact.Contact;
import com.verby.indp.domain.contact.dto.request.RegisterContactRequest;
import com.verby.indp.domain.contact.repository.ContactRepository;
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

        Mail mail = new Mail(to, "[버비] 문의가 들어왔어요!",
            "문의 내용: " + request.content() + "\n" +
            "문의자 성함: " + request.userName() + "\n" +
            "문의자 연락처: " + request.phoneNumber() + "\n");
        applicationEventPublisher.publishEvent(new MailSendEvent(mail));

        return persistContact.getContactId();
    }

}
