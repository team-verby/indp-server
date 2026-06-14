package com.verby.indp.fixture;

import com.verby.indp.domain.creator.Creator;
import org.springframework.test.util.ReflectionTestUtils;

public class CreatorFixture {

    public static Creator creator() {
        return new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "password123!");
    }

    public static Creator creatorWithId(Long id) {
        Creator creator = new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "password123!");
        ReflectionTestUtils.setField(creator, "creatorId", id);
        return creator;
    }
}
