package com.dynamics.andrzej.smart.hotel.models;

import lombok.Data;

@Data
public class RoomRequest {
    private String name;
    private int size;
    private String type;
}
