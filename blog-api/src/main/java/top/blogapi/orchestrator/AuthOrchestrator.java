package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.mapper.UserMapper;
import top.blogapi.dto.request.auth.LoginRequest;
import top.blogapi.dto.request.auth.RegisterRequest;
import top.blogapi.dto.response.AuthResponse;
import top.blogapi.dto.response.UserResponse;
import top.blogapi.model.entity.User;
import top.blogapi.model.event.UserRegisteredEvent;
import top.blogapi.repository.UserRepository;
import top.blogapi.service.auth.JwtService;

@Component
@RequiredArgsConstructor
public class AuthOrchestrator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher eventPublisher;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setDisplayName(request.getDisplayName() != null ? request.getDisplayName() : request.getUsername());
        user.setRole("USER");
        user.setIsCreator(false);
        user.setStatus("ACTIVE");
        userRepository.insert(user);

        eventPublisher.publishEvent(new UserRegisteredEvent(user.getId(), user.getUsername(), user.getEmail()));

        String token = jwtService.generateAccessToken(user.getId(), user.getRole());
        AuthResponse response = new AuthResponse();
        response.setAccessToken(token);
        response.setTokenType("Bearer");
        response.setUser(userMapper.toResponse(user));
        return response;
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        userRepository.updateLastActive(user.getId());

        String token = jwtService.generateAccessToken(user.getId(), user.getRole());
        AuthResponse response = new AuthResponse();
        response.setAccessToken(token);
        response.setTokenType("Bearer");
        response.setUser(userMapper.toResponse(user));
        return response;
    }
}
