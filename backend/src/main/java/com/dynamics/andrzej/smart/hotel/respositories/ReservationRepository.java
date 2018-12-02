package com.dynamics.andrzej.smart.hotel.respositories;

import com.dynamics.andrzej.smart.hotel.entities.Reservation;
import com.dynamics.andrzej.smart.hotel.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByRoomsId(Long roomId);

    @Query("select r from Reservation r where r.client.id = ?1")
    List<Reservation> findAllByClientId(long clientId);

    @Query("select r from Reservation r join fetch r.rooms rooms where r.fromDay >= ?1 and r.toDay <= ?2 and rooms.id = ?3 and r.id <> ?4")
    List<Reservation> findIfRoomAlreadyReserved(Date fromDay, Date toDay, Long roomId, Long exceptReservation);
}
