package com.verby.indp.domain.auth.fixture;

import com.verby.indp.domain.auth.Admin;
import java.lang.reflect.Constructor;
import org.springframework.test.util.ReflectionTestUtils;

public class AdminFixture {

    public static Admin admin() throws Exception {
        Constructor<?> constructor = Class.forName("com.verby.indp.domain.auth.Admin").getDeclaredConstructor();
        constructor.setAccessible(true);
        Admin admin = (Admin)constructor.newInstance();

        ReflectionTestUtils.setField(admin, "userId", "id");
        ReflectionTestUtils.setField(admin, "password", "1234");

        return admin;
    }
}
