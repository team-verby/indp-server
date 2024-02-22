package com.verby.indp.domain.contact.repository;

import com.verby.indp.domain.contact.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {

}
