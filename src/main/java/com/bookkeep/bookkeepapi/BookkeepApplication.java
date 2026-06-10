package com.bookkeep.bookkeepapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bookkeep.bookkeepapi.mapper")
public class BookkeepApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookkeepApplication.class, args);
    }

}
