package com.verby.indp.fixture;

import com.verby.indp.domain.auth.Owner;
import org.springframework.test.util.ReflectionTestUtils;

public class OwnerFixture {

    public static Owner owner() {
        return new Owner("store0001", "password123!", "홍길동", "010-1234-5678");
    }

    public static Owner ownerWithId(Long id) {
        Owner owner = new Owner("store0001", "password123!", "홍길동", "010-1234-5678");
        ReflectionTestUtils.setField(owner, "ownerId", id);
        return owner;
    }
}
