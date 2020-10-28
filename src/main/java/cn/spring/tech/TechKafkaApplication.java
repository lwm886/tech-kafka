package cn.spring.tech;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.spring.tech.dao")
@SpringBootApplication
public class TechKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechKafkaApplication.class, args);
	}

}
