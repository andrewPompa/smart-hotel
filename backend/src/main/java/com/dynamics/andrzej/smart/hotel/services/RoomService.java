package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.RoomSearchResult;
import com.dynamics.andrzej.smart.hotel.entities.Reservation;
import com.dynamics.andrzej.smart.hotel.entities.Room;
import com.dynamics.andrzej.smart.hotel.entities.RoomType;
import com.dynamics.andrzej.smart.hotel.respositories.ReservationRepository;
import com.dynamics.andrzej.smart.hotel.respositories.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public List<RoomSearchResult> searchRooms(Date from, Date to, int numOfRooms, RoomType prefferedType, int numOfPeoples) {
        List<Room> roomsWithoutReservation = roomRepository.findWithoutReservationBetween(from, to);

        log.info("Rooms: {}", roomsWithoutReservation.size());
        return Collections.emptyList();
    }
}
