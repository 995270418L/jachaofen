package com.chaofen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created by steve on 17-7-11.
 */
@SpringBootApplication
@EnableZuulProxy
public class ZuulRouteApplication {

    public static void main(String... args){
        SpringApplication.run(ZuulRouteApplication.class, args);
    }
}
