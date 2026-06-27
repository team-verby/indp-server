package com.verby.indp.global.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

/**
 * 민감 개인정보(주민등록번호·계좌번호 등) 양방향 암호화 유틸.
 *
 * <p>Spring Security의 {@link Encryptors#delux} (AES-256 GCM, PBKDF2 키 파생)을 사용한다.
 * 운영에서는 반드시 {@code app.security.encryption-password}/{@code app.security.encryption-salt}를
 * server-secrets에 지정해 기본값을 덮어써야 한다(기본값은 로컬/테스트 전용).
 *
 * <p>JPA {@link EncryptedStringConverter}가 정적 인스턴스를 통해 호출하므로,
 * 빈 생성 시 {@link Holder}에 자신을 등록한다.
 */
@Component
public class CryptoService {

    private final TextEncryptor encryptor;

    public CryptoService(
        @Value("${app.security.encryption-password:indp-local-dev-password}") String password,
        @Value("${app.security.encryption-salt:5c0744940b5c369b}") String salt) {
        this.encryptor = Encryptors.delux(password, salt);
        Holder.instance = this;
    }

    public String encrypt(String plain) {
        if (plain == null || plain.isBlank()) {
            return plain;
        }
        return encryptor.encrypt(plain);
    }

    public String decrypt(String cipher) {
        if (cipher == null || cipher.isBlank()) {
            return cipher;
        }
        return encryptor.decrypt(cipher);
    }

    static CryptoService instance() {
        if (Holder.instance == null) {
            throw new IllegalStateException("CryptoService가 초기화되지 않았습니다.");
        }
        return Holder.instance;
    }

    private static final class Holder {
        private static CryptoService instance;
    }
}
