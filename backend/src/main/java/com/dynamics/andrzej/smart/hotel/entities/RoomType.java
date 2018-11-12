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
}
