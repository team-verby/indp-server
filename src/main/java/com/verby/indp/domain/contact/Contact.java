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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contact")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Contact extends BaseTimeEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long contactId;

    @Embedded
    private ContactUserName userName;

    @Embedded
    private ContactContent content;

    @Embedded
    private PhoneNumber phoneNumber;

    public Contact(
        String userName,
        String content,
        String phoneNumber
    ) {
        this.userName = new ContactUserName(userName);
        this.content = new ContactContent(content);
        this.phoneNumber = new PhoneNumber(phoneNumber);
    }

    public String getUserName() {
        return userName.getUserName();
    }

    public String getContent() {
        return content.getContent();
    }

    public String getPhoneNumber() {
        return phoneNumber.getPhoneNumber();
    }
}
