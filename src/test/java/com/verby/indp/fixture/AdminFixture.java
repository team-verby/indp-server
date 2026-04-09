package com.verby.indp.fixture;

import com.verby.indp.domain.auth.Admin;
import java.lang.reflect.Constructor;
import org.springframework.test.util.ReflectionTestUtils;

public class AdminFixture {

    public static Admin admin() {
        try {
            Constructor<Admin> constructor = Admin.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Admin admin = constructor.newInstance();
            ReflectionTestUtils.setField(admin, "adminId", 1L);
            ReflectionTestUtils.setField(admin, "loginId", "admin");
            ReflectionTestUtils.setField(admin, "password", "password123!");
            return admin;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
