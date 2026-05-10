package top.blogapi.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import top.blogapi.dto.request._common.LoginRequest;
import top.blogapi.dto.request._common.LoginResponse;
import top.blogapi.model.entity.User;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthService {

    AuthenticationManager authenticationManager;

    JwtService jwtService;

//    private final LoginLogService loginLogService;

    public LoginResponse login(
            LoginRequest request,
            HttpServletRequest httpRequest
    ) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        User user = (User) authentication.getPrincipal();

        String token =
                jwtService.generateAccessToken(
                        user.getUsername(),
                        user.getAuthorities()
                );

//        loginLogService.saveSuccessLog(
//                user.getUsername(),
//                httpRequest
//        );

        LoginResponse.UserResponse userResponse =
                LoginResponse.UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .avatar(user.getAvatar())
                        .role(
                                user.getAuthorities()
                                        .stream()
                                        .findFirst()
                                        .map(GrantedAuthority::getAuthority)
                                        .orElse(null)
                        )
                        .build();

        return LoginResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }
}