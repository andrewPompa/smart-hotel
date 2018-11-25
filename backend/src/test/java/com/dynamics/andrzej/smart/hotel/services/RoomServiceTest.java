package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.SmartHotel;
import com.dynamics.andrzej.smart.hotel.entities.Room;
import com.dynamics.andrzej.smart.hotel.entities.RoomType;
import com.dynamics.andrzej.smart.hotel.respositories.RoomRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoomServiceTest {
    private RoomService roomService;
    private RoomRepository roomRepository;

    @BeforeTest
    public void setUp() {
        ConfigurableApplicationContext run = SpringApplication.run(SmartHotel.class, "");
        roomService = run.getBean(RoomService.class);
        roomRepository = run.getBean(RoomRepository.class);
    }

    @Test
    public void searchRoomTest() {
        final Calendar calendar = Calendar.getInstance();
        Date from = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 5);
        Date to = calendar.getTime();
        roomService.searchRooms(from, to, 1, RoomType.STANDARD, 10);
    }
}