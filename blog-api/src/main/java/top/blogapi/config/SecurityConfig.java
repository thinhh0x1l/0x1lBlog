package top.blogapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
import top.blogapi.filter.JwtAuthenticationFilter;
import top.blogapi.security.auth.OAuth2LoginSuccessHandler;
import top.blogapi.service.oauth2.CustomOAuth2UserService;

import java.time.Duration;
import java.util.List;

/**
 * Cấu hình Spring Security với JWT, CORS, CSRF, session stateless và phân quyền theo vai trò.
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final MyAuthenticationEntryPoint myAuthenticationEntryPoint;
    private final MyAccessDeniedHandler myAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/blogs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hashtags/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/discovery/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/statuses/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/stories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/series/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/canvases/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/profile/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tags/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/trending/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/search/**").permitAll()
                        .requestMatchers("/api/blind/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/shop/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/playlists/user/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/reputation/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/mischief/**").permitAll()
                        .requestMatchers("/api/canvases/{id}/strokes").permitAll()
                        .requestMatchers("/admin/auth/**").permitAll()
                        .requestMatchers("/admin/dashboard").permitAll()
                        .requestMatchers(HttpMethod.GET, "/admin/**").hasAnyRole("ADMIN", "VISITOR")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2LoginSuccessHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(myAuthenticationEntryPoint)
                        .accessDeniedHandler(myAccessDeniedHandler)
                );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(false);
        configuration.setAllowedOrigins(List.of(
                "https://0x1l-blog.vercel.app",
                "https://0x1l-blog-admin.vercel.app",
                "http://localhost:5174",
                "http://localhost:5173"
        ));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setMaxAge(Duration.ofSeconds(3600));
        configuration.addExposedHeader("X-Guest-Token");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
