package top.blogapi.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DatabaseChecker {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void checkConnection() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("✅ Kết nối DB thành công!");
        } catch (Exception e) {
            System.err.println("❌ Lỗi kết nối DB: " + e.getMessage());
            throw e;
        }
    }
}
