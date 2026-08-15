package com.jiege.community;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class UserAuthorApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthorApplication.class, args);
		log.info("启动成功");
	}

}
