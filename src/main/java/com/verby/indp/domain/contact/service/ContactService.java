package com.verby.indp.domain.contact.service;

import com.verby.indp.domain.contact.Contact;
import com.verby.indp.domain.contact.dto.request.RegisterContactRequest;
import com.verby.indp.domain.contact.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactService {

    private final ContactRepository contactRepository;

    @Transactional
    public long registerContact(RegisterContactRequest request) {
        Contact contact = new Contact(request.userName(), request.content(), request.phoneNumber());
        Contact persistContact = contactRepository.save(contact);

        return persistContact.getContactId();
    }

}
