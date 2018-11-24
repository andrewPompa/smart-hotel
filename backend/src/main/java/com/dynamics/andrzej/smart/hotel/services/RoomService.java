package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.entities.Reservation;
import com.dynamics.andrzej.smart.hotel.entities.Room;
import com.dynamics.andrzej.smart.hotel.respositories.ReservationRepository;
import com.dynamics.andrzej.smart.hotel.respositories.RoomRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
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
}
