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

    public int getNumOfRooms() {
        return rooms.size();
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public int getSize() {
        return rooms.stream().map(Room::getSize).reduce((o1, o2) -> o1 + o2).get();
    }
}
