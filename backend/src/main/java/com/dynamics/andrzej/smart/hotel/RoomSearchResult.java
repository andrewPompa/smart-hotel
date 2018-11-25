package com.dynamics.andrzej.smart.hotel;

import com.dynamics.andrzej.smart.hotel.entities.Room;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomSearchResult {
    private List<Room> rooms;
    private double roomsPrice;

    public RoomSearchResult(List<Room> rooms) {
        this.rooms = rooms;
    }

    public RoomSearchResult() {
        rooms = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }
}
