package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.RoomSearchResult;
import com.dynamics.andrzej.smart.hotel.entities.Reservation;
import com.dynamics.andrzej.smart.hotel.entities.Room;
import com.dynamics.andrzej.smart.hotel.entities.RoomType;
import com.dynamics.andrzej.smart.hotel.respositories.ReservationRepository;
import com.dynamics.andrzej.smart.hotel.respositories.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public RoomService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public Room add(Room room) {
        final Optional<Room> byName = roomRepository.findByName(room.getName());
        if (byName.isPresent()) {
            throw new IllegalArgumentException("Room with this name already exists");
        }
        return roomRepository.save(room);
    }

    public void delete(Long id) {
        final List<Reservation> byRoomId = reservationRepository.findByRoomId(id);
        if (!byRoomId.isEmpty()) {
            throw new IllegalArgumentException("Room is reserved, cannot by removed");
        }
        roomRepository.deleteById(id);
    }

    public List<RoomSearchResult> searchRooms(Date from, Date to, int numOfRooms, RoomType preferredType, int numOfPeoples) {
        List<Room> rooms = roomRepository.findWithoutReservationBetween(from, to);
        List<RoomSearchResult> allSolutions = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            final Room room = rooms.get(i);
            List<Room> parentRoomChain = Stream.of(room).collect(Collectors.toList());
            findCombinationOfFoundRooms(rooms.subList(i + 1, rooms.size()), numOfPeoples, parentRoomChain, allSolutions);
        }
        log.info("Rooms: {}", rooms.size());
        return allSolutions;
    }
    private void findCombinationOfFoundRooms(List<Room> rooms, int minimumSize, List<Room> roomChain, List<RoomSearchResult> allSolutions) {
        if (rooms.isEmpty()) {
            return;
        }
        final Room edge = rooms.get(0);
        final List<Room> extendedRoomChain = new ArrayList<>(roomChain);
        extendedRoomChain.add(edge);

        Integer sumSize = extendedRoomChain.stream().map(Room::getSize).reduce((i, j) -> i + j).get();
        if (sumSize >= minimumSize) {
            allSolutions.add(new RoomSearchResult(extendedRoomChain));
        } else {
            roomChain = extendedRoomChain;
        }
        for (int i = 1; i < rooms.size(); i++) {
            final Room room = rooms.get(i);
            List<Room> newRoomChain = new ArrayList<>(roomChain);
            newRoomChain.add(room);
            sumSize = newRoomChain.stream().map(Room::getSize).reduce((k, j) -> k + j).get();
            if (sumSize < minimumSize) {
                findCombinationOfFoundRooms(rooms.subList(i + 1, rooms.size()), minimumSize, newRoomChain, allSolutions);
            } else {
                allSolutions.add(new RoomSearchResult(newRoomChain));
            }
        }

    }
}
