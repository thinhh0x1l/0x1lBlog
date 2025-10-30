package top.blogapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.blogapi.entity.Blog;
import top.blogapi.entity.Category;
import top.blogapi.repository.BlogRepository;
import top.blogapi.repository.CategoryRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

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
	@Autowired
	CategoryRepository categoryRepository;



	@Test
	void test1 (){
		Optional<Blog> blog = blogRepository.getBlogById(35L);
        blog.ifPresent(blog1 -> {
			blog1.setTags(blogRepository.findTagsByBlogId(35L));
		});
		System.out.println(blog);

	}

	@Test
	void test2(){
		System.out.println(blogRepository.findTagsByBlogId(35L));
	}


	@Test
	void test3(){
//		Optional<Blog> blog = blogRepository.getBlogById(35L);
//		Category category = categoryRepository.getCategoryById(1L);
//		blog.ifPresent(blog1 -> {blog1.setTitle("aaaa");
//			blog1.setCategory(category);
//			blogRepository.updateBlog(blog1);
//
//		});
//		System.out.println(blog);
	}
//	@Test
//	void test() {
//		List<Blog> blogs = blogMapper.getBlogList();
//		System.out.println(blogs);
//	}
}
