package top.blogapi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;

@Component
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JwtUtils {
    //1000 * 60 * 60 * 24 * 3 token
    long expireTime = 259200000;
    SecretKey SECRET_KEY;


    public JwtUtils(SecretKey secretKey) {
        SECRET_KEY = secretKey;
    }

    public boolean judgeTokenIsExist(String token){
        return !StringUtils.isEmpty(token) && !"null".equals(token);
    }

    public String generateToken(String subject){
        return Jwts.builder()
                .subject(subject)
                .expiration(Date.from(Instant.now().plusSeconds(expireTime)))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String generateToken(String subject, Collection<? extends GrantedAuthority> authorities){
        StringBuilder sb = new StringBuilder();
        for (GrantedAuthority authority: authorities)
            sb.append(authority.getAuthority()).append(",");
        return Jwts.builder()
                .subject(subject)
                .claim("authorities",sb)
                .expiration(Date.from(Instant.now().plusSeconds(this.expireTime)))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String generateToken(String subject, long expireTime){
        return Jwts.builder()
                .subject(subject)
                .expiration(Date.from(Instant.now().plusSeconds(expireTime)))
                .signWith(SECRET_KEY)
                .compact();
    }

    public Claims getTokenContent(String token){
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
