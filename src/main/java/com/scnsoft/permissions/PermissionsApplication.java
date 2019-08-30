package com.scnsoft.permissions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PermissionsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PermissionsApplication.class, args);
    }
}