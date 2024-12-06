package com.verby.indp.global.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class TokenManagerTest {

    @InjectMocks
    private TokenManager tokenManager;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenManager, "SECRET_KEY", "secretKey");
    }


    @Test
    @DisplayName("createToken 메서드 실행 시, 토큰을 반환한다.")
    void createToken() {
        // given
        long id = 1;

        // when
        String result = tokenManager.createToken(id);

        // then
        assertThat(result).isInstanceOf(String.class);
    }

    @Test
    @DisplayName("decodeToken 메서드 실행 시, 토큰을 디코딩한다.")
    void decodeToken() {
        // given
        long id = 1;
        String token = tokenManager.createToken(id);

        // when
        Long result = tokenManager.decodeToken(token);

        // then
        assertThat(result).isEqualTo(id);
    }


}
