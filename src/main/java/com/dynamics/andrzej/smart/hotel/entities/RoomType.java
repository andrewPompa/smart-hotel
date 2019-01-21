package com.dynamics.andrzej.smart.hotel.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RoomType {
    STANDARD(0),
    PREMIUM(1),
    SPECIAL(2);

    @Getter
    private int id;

    public static RoomType get(String name) {
        if (STANDARD.name().equalsIgnoreCase(name)) {
            return STANDARD;
        }
        if (PREMIUM.name().equalsIgnoreCase(name)) {
            return PREMIUM;
        }
        if (SPECIAL.name().equalsIgnoreCase(name)) {
            return SPECIAL;
        }
        throw new IllegalArgumentException("name not set!");
    }
}
