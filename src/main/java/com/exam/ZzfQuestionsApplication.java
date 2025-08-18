package com.exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.exam.entity", "com.zzf.entity"})
@EnableJpaRepositories(basePackages = {"com.exam.repository", "com.zzf.repository"})
@ComponentScan(basePackages = {"com.exam", "com.zzf"})
public class ZzfQuestionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZzfQuestionsApplication.class, args);
	}

}
