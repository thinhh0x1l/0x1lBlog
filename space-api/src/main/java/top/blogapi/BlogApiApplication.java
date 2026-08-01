package top.blogapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Điểm vào chính của ứng dụng Spring Boot Blog API.
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("top.blogapi")
public class BlogApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(BlogApiApplication.class, args);
		System.out.println("HOST = " + System.getenv("MYSQLHOST"));
	}
}
