package com.dynamics.andrzej.smart.hotel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SmartHotel {

    public static void main(String[] args) {
        log.info("Starting Smart Hotel Application...");
        SpringApplication.run(SmartHotel.class, args);
    }
}
