package com.example.mscart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsCartApplication.class, args);
    }

}
