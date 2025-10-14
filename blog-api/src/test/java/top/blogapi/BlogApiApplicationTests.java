package top.blogapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.blogapi.repository.BlogRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class BlogApiApplicationTests {

	@Test
	void contextLoads() {
	}
	@Autowired
	private DataSource dataSource;

	@Test
	void testConnection() throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			System.out.println("✅ Connected to DB: " + conn.getMetaData().getURL());
		}
	}
	@Autowired
	BlogRepository blogRepository;

//	@Test
//	void test() {
//		List<Blog> blogs = blogMapper.getBlogList();
//		System.out.println(blogs);
//	}
}
