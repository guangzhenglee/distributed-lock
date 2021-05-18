package com.ligz.lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LockAwesomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LockAwesomeApplication.class, args);
    }

}
