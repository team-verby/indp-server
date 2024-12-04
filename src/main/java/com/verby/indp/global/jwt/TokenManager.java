package com.verby.indp.global.jwt;

import com.verby.indp.domain.common.exception.AuthException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenManager {

    private static final long TOKEN_VALIDITY_TIME = 30 * 60 * 1000;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String createToken(Long id) {
        Date date = new Date();
        return Jwts.builder()
            .setIssuedAt(date)
            .setExpiration(new Date(date.getTime() + TOKEN_VALIDITY_TIME))
            .claim("adminId", id)
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }

    public Long decodeToken(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("adminId", Long.class);
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthException("유효하지 않은 토큰입니다.");
        }
    }
}
