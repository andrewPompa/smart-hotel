package com.dynamics.andrzej.smart.hotel.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/room")
@Slf4j
public class SearchRoomController {

    @GetMapping("/search")
    public void test(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("standard") String standard,
            @RequestParam("numOfPeoples") Integer numOfPeoples
    ) {
        log.info("got it!");
    }
}
