package com.dynamics.andrzej.smart.hotel;

import com.dynamics.andrzej.smart.hotel.entities.Room;
import lombok.Data;

import java.util.List;

@Data
public class RoomSearchResult {
    private List<Room> rooms;
    private double roomsPrice;
}
