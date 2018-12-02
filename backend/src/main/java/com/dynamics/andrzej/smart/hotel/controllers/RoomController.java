package com.dynamics.andrzej.smart.hotel.controllers;

import com.dynamics.andrzej.smart.hotel.entities.Room;
import com.dynamics.andrzej.smart.hotel.models.RoomRequest;
import com.dynamics.andrzej.smart.hotel.services.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/room")
@Slf4j
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/all")
    public List<Room> getClients() {
        return roomService.findAll();
    }

    @PostMapping
    public Room add(@RequestBody RoomRequest roomRequest) {
       return roomService.add(roomRequest);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        log.info("info: {}", id);
        roomService.delete(id);
        return true;
    }
}
