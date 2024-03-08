package com.verby.indp.domain.contact.event;

import com.verby.indp.domain.notification.dto.ContactMail;

public record ContactMailEvent(
    ContactMail request
) {

}
