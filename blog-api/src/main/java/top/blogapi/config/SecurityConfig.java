package top.blogapi.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import top.blogapi.config.authentication.MyAuthenticationEntryPoint;
import top.blogapi.config.authentication.MyAuthenticationFailureHandler;
import top.blogapi.config.authentication.MyAuthenticationSuccessHandler;
import top.blogapi.service.UserServiceImpl;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .anyRequest().authenticated()
                ).formLogin(
                        formLogin -> formLogin
                                .loginProcessingUrl("/admin/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .successHandler(new MyAuthenticationSuccessHandler())
                                .failureHandler(new MyAuthenticationFailureHandler())
                                .permitAll()
                ).logout(LogoutConfigurer::permitAll)
                .exceptionHandling(
                        ex->ex
                                .authenticationEntryPoint(new MyAuthenticationEntryPoint()));
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManager =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManager
                .userDetailsService(new UserServiceImpl())
                .passwordEncoder(passwordEncoder());
        return authenticationManager.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MyPasswordEncoder();
    }
}
