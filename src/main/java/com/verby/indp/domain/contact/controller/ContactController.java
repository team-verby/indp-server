package com.verby.indp.domain.contact.controller;

import com.verby.indp.domain.contact.dto.request.RegisterContactRequest;
import com.verby.indp.domain.contact.service.ContactService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/contacts")
    public ResponseEntity<Void> registerContact(@RequestBody RegisterContactRequest request) {
        long contactId = contactService.registerContact(request);
        URI uri = URI.create("/api/contacts/" + contactId);

        return ResponseEntity.created(uri).build();
    }

}
