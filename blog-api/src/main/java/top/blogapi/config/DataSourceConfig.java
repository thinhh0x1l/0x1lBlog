package top.blogapi.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;

@Configuration
@MapperScan(basePackages = "top.blogapi.mapper")
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/blog");
        dataSource.setUsername("root");
        dataSource.setPassword("1820");

        // ========== CẤU HÌNH HIKARI CP CONNECTION POOL ==========

        // Số lượng kết nối tối đa trong pool (cho phép 20 kết nối đồng thời)
        dataSource.setMaximumPoolSize(20);
        // Số kết nối tối thiểu luôn duy trì sẵn (tránh overhead tạo kết nối mới)
        dataSource.setMinimumIdle(5);
        // Thời gian tối đa chờ để lấy kết nối từ pool (30 giây)
        dataSource.setConnectionTimeout(30000);
        // Thời gian một kết nối không sử dụng trước khi bị đóng (10 phút)
        dataSource.setIdleTimeout(600000);
        // Thời gian tối đa một kết nối tồn tại (30 phút), tránh memory leaks
        dataSource.setMaxLifetime(1800000);
        // Tên pool để nhận diện khi monitoring
        dataSource.setPoolName("MyBatisHikariPool");

        // ========== TỐI ƯU HOÁ HIỆU SUẤT CHO MYSQL ==========

        // Bật cache cho prepared statements - giảm việc compile SQL lặp lại
        dataSource.addDataSourceProperty("cachePrepStmts", "true");
        // Số lượng prepared statements được cache (250 statements)
        dataSource.addDataSourceProperty("prepStmtCacheSize", "250");
        // Kích thước tối đa của SQL statement được cache (2048 ký tự)
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        // Sử dụng server-side prepared statements - tăng hiệu suất
        dataSource.addDataSourceProperty("useServerPrepStmts", "true");
        // Giảm round-trips đến database bằng cách sử dụng session state local
        dataSource.addDataSourceProperty("useLocalSessionState", "true");
        // Tối ưu batch operations - chuyển multiple inserts thành single batch
        dataSource.addDataSourceProperty("rewriteBatchedStatements", "true");
        // Cache metadata của result set - giảm database metadata queries
        dataSource.addDataSourceProperty("cacheResultSetMetadata", "true");
        // Cache cấu hình server - tránh query cấu hình lặp lại
        dataSource.addDataSourceProperty("cacheServerConfiguration", "true");
        // Giảm số lượng auto-commit calls đến database
        dataSource.addDataSourceProperty("elideSetAutoCommits", "true");
        // Tắt thống kê thời gian để tăng hiệu suất
        dataSource.addDataSourceProperty("maintainTimeStats", "false");

        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        // Gán DataSource cho SqlSessionFactory
        sessionFactory.setDataSource(dataSource());

        // ========== CẤU HÌNH MYBATIS ==========

        org.apache.ibatis.session.Configuration mybatisConfig = new org.apache.ibatis.session.Configuration();

        // Tự động map underscore naming (db) sang camelCase (java)
        // Ví dụ: user_name → userName, created_at → createdAt
        mybatisConfig.setMapUnderscoreToCamelCase(true);
        // Bật cache level 2 - cache kết quả query
        mybatisConfig.setCacheEnabled(true);
        // Bật lazy loading - chỉ load related objects khi cần thiết
        mybatisConfig.setLazyLoadingEnabled(true);
        // Tắt aggressive lazy loading - không load tất cả relationships ngay lập tức
        mybatisConfig.setAggressiveLazyLoading(false);
        // Các method trigger lazy loading (chỉ load khi gọi các method này)
        mybatisConfig.setLazyLoadTriggerMethods(new HashSet<>(List.of("equals","clone","hashCode","toString")));
        // Hỗ trợ multiple result sets (cho stored procedures trả về nhiều kết quả)
        mybatisConfig.setMultipleResultSetsEnabled(true);
        // Sử dụng column label thay vì column name (phù hợp với alias trong SQL)
        mybatisConfig.setUseColumnLabel(true);
        // Hỗ trợ auto-generated keys (cho ID tự động tăng)
        mybatisConfig.setUseGeneratedKeys(true);
        // Timeout mặc định cho mỗi query (25 giây)
        mybatisConfig.setDefaultStatementTimeout(25);
        // Số records fetch mỗi lần từ database (tối ưu memory usage)
        mybatisConfig.setDefaultFetchSize(100);
        // Prefix cho log messages (dễ dàng filter log)
        mybatisConfig.setLogPrefix("mybatis.");

        // Áp dụng cấu hình MyBatis
        sessionFactory.setConfiguration(mybatisConfig);

        // Đăng ký package chứa entity classes
        // Cho phép sử dụng tên class thay vì fully qualified name trong mapper
        sessionFactory.setTypeAliasesPackage("com.example.demo.model");

        return sessionFactory.getObject();
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        // Quản lý transactions cho MyBatis
        // Đảm bảo các operations trong cùng transaction sẽ commit/rollback together
        return new DataSourceTransactionManager(dataSource());
    }
}
