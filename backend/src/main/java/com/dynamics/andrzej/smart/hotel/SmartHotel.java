package com.dynamics.andrzej.smart.hotel;

import com.dynamics.andrzej.smart.hotel.entities.Receptionist;
import com.dynamics.andrzej.smart.hotel.entities.Room;
import com.dynamics.andrzej.smart.hotel.entities.RoomType;
import com.dynamics.andrzej.smart.hotel.respositories.ReceptionistRepository;
import com.dynamics.andrzej.smart.hotel.services.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Slf4j
public class SmartHotel {
    private final ReceptionistRepository receptionistRepository;
    private final RoomService roomService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SmartHotel(ReceptionistRepository receptionistRepository, RoomService roomService, PasswordEncoder passwordEncoder) {
        this.receptionistRepository = receptionistRepository;
        this.roomService = roomService;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        log.info("Starting Smart Hotel Application...");
        SpringApplication.run(SmartHotel.class, args);
    }

    @PostConstruct
    public void addUsers() {
        addReceptionists();
        addRooms();
    }

    private void addReceptionists() {
        final Receptionist firstReceptionist = new Receptionist();
        firstReceptionist.setFirstName("Garol");
        firstReceptionist.setLastName("Brezes");
        firstReceptionist.setLogin("garol@brezes.pl");
        firstReceptionist.setPassword(passwordEncoder.encode("garol"));
        receptionistRepository.save(firstReceptionist);

        final Receptionist secondReceptionist = new Receptionist();
        secondReceptionist.setFirstName("Andrzej");
        secondReceptionist.setLastName("Pompa");
        secondReceptionist.setLogin("user@user.pl");
        secondReceptionist.setPassword(passwordEncoder.encode("user"));
        receptionistRepository.save(secondReceptionist);
    }
    private void addRooms() {
        final Room room100 = new Room();
        room100.setName("100a");
        room100.setSize(1);
        room100.setType(RoomType.STANDARD);
        roomService.add(room100);


        final Room room102 = new Room();
        room102.setName("102a");
        room102.setSize(2);
        room102.setType(RoomType.STANDARD);
        roomService.add(room102);

        final Room room103 = new Room();
        room103.setName("103");
        room103.setSize(3);
        room103.setType(RoomType.STANDARD);
        roomService.add(room103);

        final Room room104 = new Room();
        room104.setName("104b");
        room104.setSize(4);
        room104.setType(RoomType.STANDARD);
        roomService.add(room104);

        final Room room201 = new Room();
        room201.setName("201");
        room201.setSize(1);
        room201.setType(RoomType.PREMIUM);
        roomService.add(room201);

        final Room room202 = new Room();
        room202.setName("202");
        room202.setSize(2);
        room202.setType(RoomType.PREMIUM);
        roomService.add(room202);

        final Room room203 = new Room();
        room203.setName("203");
        room203.setSize(3);
        room203.setType(RoomType.PREMIUM);
        roomService.add(room203);

        final Room room204 = new Room();
        room204.setName("204");
        room204.setSize(4);
        room204.setType(RoomType.PREMIUM);
        roomService.add(room204);
    }
}
