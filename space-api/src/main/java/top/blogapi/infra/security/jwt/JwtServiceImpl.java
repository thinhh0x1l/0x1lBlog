package top.blogapi.infra.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private final byte[] secretKey;
    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtServiceImpl(
            @Value("${jwt.secret-key}") String secret,
            @Value("${jwt.access-expiration}") long accessExpiration,
            @Value("${jwt.refresh-expiration:2592000000}") long refreshExpiration) {
        this.secretKey = secret.getBytes();
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    @Override
    public String generateAccessToken(Long userId, String role) {
        return generateToken(userId.toString(), role, accessExpiration);
    }

    @Override
    public String generateRefreshToken(Long userId) {
        return generateToken(userId.toString(), null, refreshExpiration);
    }

    private String generateToken(String subject, String role, long expirationMs) {
        try {
            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                    .subject(subject)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + expirationMs));
            if (role != null) {
                claimsBuilder.claim("role", role);
            }
            JWTClaimsSet claims = claimsBuilder.build();
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claims
            );
            signedJWT.sign(new MACSigner(secretKey));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to generate JWT", e);
        }
    }

    @Override
    public Long getUserIdFromToken(String token) {
        return Long.parseLong(parseAndVerify(token).getSubject());
    }

    @Override
    public String getRoleFromToken(String token) {
        try {
            return parseAndVerify(token).getStringClaim("role");
        } catch (java.text.ParseException e) {
            return null;
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            parseAndVerify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private JWTClaimsSet parseAndVerify(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            MACVerifier verifier = new MACVerifier(secretKey);
            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("Invalid JWT signature");
            }
            return signedJWT.getJWTClaimsSet();
        } catch (java.text.ParseException | JOSEException e) {
            throw new RuntimeException("Failed to parse JWT", e);
        }
    }
}
