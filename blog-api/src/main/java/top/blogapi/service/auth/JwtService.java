package top.blogapi.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.io.Encoders;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import java.util.UUID;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtService {
    @Value("${jwt.access-expiration}")
    long accessExpiration;

    SecretKey key;

    @PostConstruct
    public void init() {

        this.key = Jwts.SIG.HS512.key().build();
        String secret = Encoders.BASE64.encode(key.getEncoded());

        System.out.println(secret);
    }

    public String generateAccessToken(
            String username,
            Collection<? extends GrantedAuthority> authorities
    ) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessExpiration);

        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(username)
                .issuer("blog-api")
                .issuedAt(now)
                .expiration(expiry)
                .id(UUID.randomUUID().toString())
                .claim("type", "access")
                .claim("roles", roles)
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(removeBearer(token))
                .getPayload();
    }

    public boolean isValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaims(token)
                .getSubject();
    }

    private String removeBearer(String token) {
        if (token == null)
            return null;

        return token.startsWith("Bearer ")
                ? token.substring(7)
                : token;
    }
}