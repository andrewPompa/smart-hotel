package com.dynamics.andrzej.smart.hotel.respositories;

import com.dynamics.andrzej.smart.hotel.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByName(String name);
}
