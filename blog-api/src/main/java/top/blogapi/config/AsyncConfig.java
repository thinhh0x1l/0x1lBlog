package top.blogapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Cấu hình thực thi bất đồng bộ và hỗ trợ tác vụ định kỳ.
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig {
}
