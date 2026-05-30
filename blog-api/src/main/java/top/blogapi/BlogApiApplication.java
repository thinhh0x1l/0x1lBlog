package top.blogapi;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BlogApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(BlogApiApplication.class, args);
		System.out.println("HOST = " + System.getenv("MYSQLHOST"));
	}
}
