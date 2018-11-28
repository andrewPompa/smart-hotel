package com.dynamics.andrzej.smart.hotel.services;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NewClientPinGenerator {
    private final int pinLength;

    public NewClientPinGenerator(int pinLength) {
        this.pinLength = pinLength;
    }

    public String generate() {
        return IntStream.range(0, pinLength)
                .mapToObj(i -> String.valueOf((int) (Math.random() * 10)))
                .collect(Collectors.joining());
    }
}
