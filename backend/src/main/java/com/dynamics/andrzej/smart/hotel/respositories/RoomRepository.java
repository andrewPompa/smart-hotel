package com.dynamics.andrzej.smart.hotel.respositories;

import com.dynamics.andrzej.smart.hotel.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByName(String name);

    @Query("select r from Room r left join fetch r.reservations res where res is null or res.toDay < ?2 OR res.fromDay > ?1 ORDER BY r.size desc ")
    List<Room> findWithoutReservationBetween(Date from, Date to);

    @Query("select r from Room r left join fetch r.reservations")
    List<Room> findAllEagerly();

    List<Room> findByIdIn(List<Long> roomIds);
}
