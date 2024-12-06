package com.verby.indp.global.fixture;

import com.verby.indp.global.jwt.TokenManager;
import org.springframework.test.util.ReflectionTestUtils;

public class TokenFixture {

    private static final long MOCK_ADMIN_ID = 1L;

    public static String accessToken() {
        TokenManager tokenManager = new TokenManager();
        ReflectionTestUtils.setField(tokenManager, "SECRET_KEY", "secretKey");
        return tokenManager.createToken(MOCK_ADMIN_ID);
    }

}
