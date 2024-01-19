package com.verby.indp.domain.contact;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.vo.PhoneNumber;
import com.verby.indp.domain.contact.vo.ContactContent;
import com.verby.indp.domain.contact.vo.ContactUserName;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contact")
public class Contact extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private long contactId;

    @Embedded
    private ContactUserName userName;

    @Embedded
    private ContactContent content;

    @Embedded
    private PhoneNumber phoneNumber;

}
