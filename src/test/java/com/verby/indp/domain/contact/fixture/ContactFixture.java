package com.verby.indp.domain.contact.fixture;

import com.verby.indp.domain.contact.Contact;

public class ContactFixture {

    private static final String USER_NAME = "버비";
    private static final String CONTENT = "contactContent";
    private static final String PHONE_NUMBER = "01012345678";

    public static Contact contact() {
        return new Contact(USER_NAME, CONTENT, PHONE_NUMBER);
    }

}
