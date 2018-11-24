package com.dynamics.andrzej.smart.hotel.respositories;

import com.dynamics.andrzej.smart.hotel.entities.Reservation;
import com.dynamics.andrzej.smart.hotel.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByRoomId(Long roomId);
}
