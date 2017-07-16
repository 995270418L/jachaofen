package com.jachaofen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by steve on 17-7-11.
 */

@SpringBootApplication
@EnableEurekaClient
public class UrlProductApplication {

    public static void main(String... args){
        SpringApplication.run(UrlProductApplication.class, args);
    }
}
