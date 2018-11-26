package com.dynamics.andrzej.smart.hotel.controllers;

import com.dynamics.andrzej.smart.hotel.RoomSearchResult;
import com.dynamics.andrzej.smart.hotel.entities.RoomType;
import com.dynamics.andrzej.smart.hotel.services.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


@RestController
@RequestMapping("/api/room")
@Slf4j
public class SearchRoomController {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-DD");
    private final RoomService roomService;

    public SearchRoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/search")
    public List<RoomSearchResult> searchRooms(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("standard") String standard,
            @RequestParam("numOfPeoples") Integer numOfPeoples
    ) throws ParseException {
        return roomService.searchRooms(
                dateFormat.parse(from),
                dateFormat.parse(to),
                "PREMIUM".equals(standard.toUpperCase()) ? RoomType.PREMIUM : RoomType.STANDARD,
                numOfPeoples
        );
    }
}
