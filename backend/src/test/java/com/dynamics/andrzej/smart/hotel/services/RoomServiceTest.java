package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.SmartHotel;
import com.dynamics.andrzej.smart.hotel.entities.Room;
import com.dynamics.andrzej.smart.hotel.entities.RoomType;
import com.dynamics.andrzej.smart.hotel.models.ReservationRequest;
import com.dynamics.andrzej.smart.hotel.respositories.ReservationRepository;
import com.dynamics.andrzej.smart.hotel.respositories.RoomRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoomServiceTest {
    private RoomService roomService;
    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;

    @BeforeTest
    public void setUp() {
        ConfigurableApplicationContext run = SpringApplication.run(SmartHotel.class, "");
        roomService = run.getBean(RoomService.class);
        roomRepository = run.getBean(RoomRepository.class);
        reservationRepository = run.getBean(ReservationRepository.class);
    }


    @Test
    public void addDeleteRoomTest(){
        final Room room123 = new Room();
        room123.setName("123");
        room123.setSize(2);
        room123.setType(RoomType.STANDARD);
        final List<Room> beforeRoomAdd = roomRepository.findAll();
        int initialSize = beforeRoomAdd.size();
        roomService.add(room123);
        final List<Room> afterRoomAdd= roomRepository.findAll();
        Assert.assertEquals(afterRoomAdd.size(),initialSize +1 );
        long idOfAddedRoom = 0;
        for (Room r : afterRoomAdd){
            if(room123.getName().equals(r.getName())){
                idOfAddedRoom = r.getId();
            }
        }
        roomService.delete(idOfAddedRoom);
        final List<Room> afterRoomDeletion= roomRepository.findAll();
        Assert.assertEquals(afterRoomDeletion.size(),initialSize );
    }

    @Test
    public void searchRoomTest() {
        final Calendar calendar = Calendar.getInstance();
        Date from = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 5);
        Date to = calendar.getTime();
        roomService.searchRooms(from, to, RoomType.STANDARD, 10);
    }

    @Test
    public void findRoomWithoutReservationTest() {
        final ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setEmail("test@test.pl");
        reservationRequest.setFirstName("Testyn");
        reservationRequest.setLastName("Testowy");

        final List<Room> all = roomRepository.findAll();
        reservationRequest.setRoomIds(Stream.of(all.get(0).getId(), all.get(1).getId()).collect(Collectors.toList()));
        reservationRequest.setFrom(new Date(48, 12, 2));
        reservationRequest.setTo(new Date(48, 12, 10));
        roomService.reserve(reservationRequest);

        final Date from = new Date(48, 12, 7);
        final Date to = new Date(48, 12, 15);
        final List<Room> withoutReservationBetween = roomRepository.findWithoutReservationBetween(from, to);
        Assert.assertEquals(withoutReservationBetween.size() + 2, all.size());
    }

}