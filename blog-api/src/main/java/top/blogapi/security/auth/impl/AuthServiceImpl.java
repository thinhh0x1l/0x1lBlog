package top.blogapi.security.auth.impl;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.blogapi.model.entity.User;
import top.blogapi.repository.UserRepository;
import top.blogapi.security.UserPrincipal;
import top.blogapi.security.auth.AuthService;

@Service
@RequiredArgsConstructor
/**
 * Triển khai AuthService tải thông tin người dùng theo email
 * cho xác thực Spring Security.
 */
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return UserPrincipal.create(user);
    }
}
