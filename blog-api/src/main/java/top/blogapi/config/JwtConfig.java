package top.blogapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import top.blogapi.util.JwtUtils;

import javax.crypto.SecretKey;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class JwtConfig {


    ObjectMapper objectMapper;
    //SecretKey cho jwt
    @Bean
    public SecretKey secretKey(){
        return Jwts.SIG.HS512.key().build();
    }
    @Bean
    public JwtUtils jwtUtils(SecretKey secretKey){
        return new JwtUtils(secretKey);
    }

    @Bean
    public JwtFilter jwtFilter(ObjectMapper objectMapper, JwtUtils jwtUtils) {
        return new JwtFilter(objectMapper, jwtUtils);
    }

    @Bean
    public JwtLoginFilter jwtLoginFilter(AuthenticationManager authenticationManager,
                                         ObjectMapper objectMapper,
                                         JwtUtils jwtUtils) {
        return new JwtLoginFilter("/admin/login", authenticationManager, objectMapper, jwtUtils);
    }
}
