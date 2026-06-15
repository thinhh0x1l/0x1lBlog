package top.blogapi.service.auth;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.text.ParseException;
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

    @Value("${jwt.secret-key}")
    String secretKey;

    JWSSigner signer;
    JWSVerifier verifier;

    @PostConstruct
    public void init() throws JOSEException {
        byte[] keyBytes = secretKey.getBytes();
        if (keyBytes.length < 32) {
            byte[] padded = new byte[32];
            System.arraycopy(keyBytes, 0, padded, 0, Math.min(keyBytes.length, 32));
            keyBytes = padded;
        }
        this.signer = new MACSigner(keyBytes);
        this.verifier = new MACVerifier(keyBytes);
    }

    public String generateAccessToken(
            String username,
            Collection<? extends GrantedAuthority> authorities
    ) throws JOSEException {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessExpiration);

        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("blog-api")
                .issueTime(now)
                .expirationTime(expiry)
                .jwtID(UUID.randomUUID().toString())
                .claim("type", "access")
                .claim("roles", roles)
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet
        );
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    public JWTClaimsSet extractClaims(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(removeBearer(token));
        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("Invalid JWT signature");
        }
        return signedJWT.getJWTClaimsSet();
    }

    public boolean isValid(String token) {
        try {
            extractClaims(token);
            return !isExpired(token);
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isExpired(String token) throws ParseException, JOSEException {
        Date expiration = extractClaims(token).getExpirationTime();
        return expiration != null && expiration.before(new Date());
    }

    public String extractUsername(String token) throws ParseException, JOSEException {
        return extractClaims(token).getSubject();
    }

    private String removeBearer(String token) {
        if (token == null)
            return null;

        return token.startsWith("Bearer ")
                ? token.substring(7)
                : token;
    }
}