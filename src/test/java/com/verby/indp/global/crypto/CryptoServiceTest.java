package com.verby.indp.global.crypto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CryptoServiceTest {

    private CryptoService cryptoService;

    @BeforeEach
    void setUp() {
        // 생성 시 정적 Holder에 자신을 등록한다(EncryptedStringConverter가 사용).
        cryptoService = new CryptoService("test-password", "5c0744940b5c369b");
    }

    @Test
    @DisplayName("성공 : 암호화 후 복호화하면 원문이 복원된다.")
    void encryptDecryptRoundTrip() {
        String plain = "9010101234567";

        String cipher = cryptoService.encrypt(plain);

        assertThat(cipher).isNotNull().isNotEqualTo(plain);
        assertThat(cryptoService.decrypt(cipher)).isEqualTo(plain);
    }

    @Test
    @DisplayName("성공 : null·빈 값은 그대로 반환한다.")
    void passThroughNullAndBlank() {
        assertThat(cryptoService.encrypt(null)).isNull();
        assertThat(cryptoService.encrypt("")).isEmpty();
        assertThat(cryptoService.decrypt(null)).isNull();
        assertThat(cryptoService.decrypt(" ")).isEqualTo(" ");
    }

    @Test
    @DisplayName("성공 : EncryptedStringConverter가 DB 저장/조회 시 암복호화한다.")
    void converterRoundTrip() {
        EncryptedStringConverter converter = new EncryptedStringConverter();
        String plain = "12345678901234";

        String dbColumn = converter.convertToDatabaseColumn(plain);

        assertThat(dbColumn).isNotEqualTo(plain);
        assertThat(converter.convertToEntityAttribute(dbColumn)).isEqualTo(plain);
    }
}
