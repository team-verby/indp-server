package com.verby.indp.fixture;

import com.verby.indp.domain.auth.User;
import org.springframework.test.util.ReflectionTestUtils;

public class UserFixture {

    public static User user() {
        return new User("parkwan123", "password123!", "박완", "parkwan@example.com");
    }

    public static User userWithId(Long id) {
        User user = new User("parkwan123", "password123!", "박완", "parkwan@example.com");
        ReflectionTestUtils.setField(user, "userId", id);
        return user;
    }
}
