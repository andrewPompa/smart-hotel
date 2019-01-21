package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.SmartHotel;
import com.dynamics.andrzej.smart.hotel.entities.Reservation;
import com.dynamics.andrzej.smart.hotel.entities.Room;
import com.dynamics.andrzej.smart.hotel.models.ReservationUpdateRequest;
import com.dynamics.andrzej.smart.hotel.respositories.ReservationRepository;
import com.dynamics.andrzej.smart.hotel.respositories.RoomRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static org.testng.Assert.*;

public class ReservationServiceTest {
    private RoomService roomService;
    private ReservationService reservationService;
    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;

    @BeforeTest
    public void setUp() {
        ConfigurableApplicationContext run = SpringApplication.run(SmartHotel.class, "");
        roomService = run.getBean(RoomService.class);
        roomRepository = run.getBean(RoomRepository.class);
        reservationRepository = run.getBean(ReservationRepository.class);
        reservationService = run.getBean(ReservationService.class);
    }

    @Test
    public void testUpdate() {
        // to do: further developement
        // assert 1==1;
    }

    @Test
    public void testDelete() {
        // to do: further developement
        assert 1==1;
    }
}