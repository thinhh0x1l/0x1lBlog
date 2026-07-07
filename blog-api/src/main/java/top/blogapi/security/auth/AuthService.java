package top.blogapi.security.auth;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Giao diện service xác thực, mở rộng UserDetailsService của Spring Security
 * để tải thông tin người dùng theo email.
 */
public interface AuthService extends UserDetailsService {
}
