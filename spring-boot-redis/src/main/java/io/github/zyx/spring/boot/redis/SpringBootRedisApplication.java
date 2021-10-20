package io.github.zyx.spring.boot.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringBootRedisApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(SpringBootRedisApplication.class, args);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
