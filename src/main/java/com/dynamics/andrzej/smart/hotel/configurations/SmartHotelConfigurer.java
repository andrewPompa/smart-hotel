package com.dynamics.andrzej.smart.hotel.configurations;

import com.dynamics.andrzej.smart.hotel.services.NewClientPinGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmartHotelConfigurer {

    @Bean
    public NewClientPinGenerator pinGenerator() {
        return new NewClientPinGenerator(6);
    }
}
