package top.blogapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import top.blogapi.service.impl.UserServiceImpl;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class SecurityConfig {
    UserServiceImpl userService;
    MyAuthenticationEntryPoint myAuthenticationEntryPoint;
    ObjectMapper objectMapper;


    //SecretKey cho jwt
    @Bean
    public SecretKey secretKey(){
        return Jwts.SIG.HS512.key().build();
    }
    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(secretKey(), objectMapper);
    }
    @Bean
    public JwtLoginFilter jwtLoginFilter(AuthenticationManager authenticationManager) {
        long EXPIRE_TIME = 3600 * 6;
        return new JwtLoginFilter("/admin/login",authenticationManager,secretKey(),objectMapper, EXPIRE_TIME);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtLoginFilter jwtLoginFilter,
                                                   JwtFilter jwtFilter) throws Exception {
        http
                .cors(cors->cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorizeRequests -> authorizeRequests

                        .anyRequest().authenticated()
                )
                //filter JWT tùy chỉnh
                .addFilterBefore(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, JwtLoginFilter.class)

                .exceptionHandling(ex->ex
                        .authenticationEntryPoint(myAuthenticationEntryPoint)
                );
        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setMaxAge(Duration.ofSeconds(3600)); //cache preflight 1h

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManager =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManager
//                .userDetailsService(userService)
//                .passwordEncoder(passwordEncoder());
//        return authenticationManager.build();
//    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
