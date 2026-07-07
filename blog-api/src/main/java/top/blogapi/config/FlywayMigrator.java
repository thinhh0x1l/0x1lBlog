package top.blogapi.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Thực thi migration cơ sở dữ liệu Flyway khi khởi động ứng dụng.
 */
//@Component
//@Order(1)
//@RequiredArgsConstructor
//@Slf4j
//public class FlywayMigrator implements CommandLineRunner {
//
//    private final DataSource dataSource;
//
//    @Override
//    public void run(String... args) {
//        Flyway flyway = Flyway.configure()
//                .dataSource(dataSource)
//                .locations("classpath:db/migration")
//                .baselineOnMigrate(true)
//                .load();
//        flyway.migrate();
//        log.info("Flyway migrations completed");
//    }
//}
