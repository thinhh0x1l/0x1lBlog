package top.blogapi.config;

import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;

/**
 * Cấu hình MyBatis {@link SqlSessionFactory} và transaction manager.
 */
@Configuration
@MapperScan(basePackages = "top.blogapi.repository")
public class DataSourceConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        org.apache.ibatis.session.Configuration mybatisConfig = new org.apache.ibatis.session.Configuration();
        mybatisConfig.setMapUnderscoreToCamelCase(true);
        mybatisConfig.setCacheEnabled(true);
        mybatisConfig.setLazyLoadingEnabled(true);
        mybatisConfig.setAggressiveLazyLoading(false);
        mybatisConfig.setLazyLoadTriggerMethods(new HashSet<>(List.of("equals","clone","hashCode","toString")));
        mybatisConfig.setUseColumnLabel(true);
        mybatisConfig.setUseGeneratedKeys(true);
        mybatisConfig.setDefaultStatementTimeout(25);
        mybatisConfig.setDefaultFetchSize(100);
        mybatisConfig.setLogPrefix("mybatis.");
        mybatisConfig.setLogImpl(Slf4jImpl.class);

        sessionFactory.setConfiguration(mybatisConfig);
        sessionFactory.setTypeAliasesPackage("top.blogapi.model.entity");

        return sessionFactory.getObject();
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
