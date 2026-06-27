package com.verby.indp.global.crypto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * 엔티티 필드를 DB 저장 시 AES 암호화하고, 조회 시 복호화하는 JPA 컨버터.
 * 민감 개인정보 컬럼(주민등록번호·계좌번호)에 {@code @Convert}로 적용한다.
 */
@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return CryptoService.instance().encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return CryptoService.instance().decrypt(dbData);
    }
}
