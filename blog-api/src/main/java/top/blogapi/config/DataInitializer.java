package top.blogapi.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import top.blogapi.model.entity.User;
import top.blogapi.repository.UserRepository;

/**
 * Khởi tạo dữ liệu mặc định cho người dùng admin khi chạy lần đầu.
 */
@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {
            if (!userRepository.existsByEmail("admin@0x1lblog.top")) {
                User admin = new User();
                admin.setEmail("admin@0x1lblog.top");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setDisplayName("Admin");
                admin.setRole("ADMIN");
                admin.setIsCreator(false);
                admin.setStatus("ACTIVE");
                userRepository.insert(admin);
                log.info("Default admin user created");
            }
        } catch (Exception e) {
            log.warn("DataInitializer skipped (tables not ready yet): {}", e.getMessage());
        }
    }
}
