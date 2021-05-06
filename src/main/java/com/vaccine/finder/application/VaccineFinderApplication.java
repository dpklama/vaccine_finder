package com.vaccine.finder.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.vaccine")
@EnableScheduling
public class VaccineFinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaccineFinderApplication.class, args);
    }

}
