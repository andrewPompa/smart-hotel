package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.RoomSearchResult;
import com.dynamics.andrzej.smart.hotel.entities.*;
import com.dynamics.andrzej.smart.hotel.models.ReservationRequest;
import com.dynamics.andrzej.smart.hotel.respositories.ReservationRepository;
import com.dynamics.andrzej.smart.hotel.respositories.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ClientService clientService;
    private final ReservationPriceCalculator reservationPriceCalculator;
    private final NewClientPinGenerator pinGenerator;

    @Autowired
    public RoomService(RoomRepository roomRepository, ReservationRepository reservationRepository, ClientService clientService, ReservationPriceCalculator reservationPriceCalculator, NewClientPinGenerator pinGenerator) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.clientService = clientService;
        this.reservationPriceCalculator = reservationPriceCalculator;
        this.pinGenerator = pinGenerator;
    }

    public Room add(Room room) {
        final Optional<Room> byName = roomRepository.findByName(room.getName());
        if (byName.isPresent()) {
            throw new IllegalArgumentException("Room with this name already exists");
        }
        return roomRepository.save(room);
    }

    public void delete(Long id) {
        final List<Reservation> byRoomId = reservationRepository.findByRoomsId(id);
        if (!byRoomId.isEmpty()) {
            throw new IllegalArgumentException("Room is reserved, cannot by removed");
        }
        roomRepository.deleteById(id);
    }

    public List<RoomSearchResult> searchRooms(Date from, Date to, RoomType preferredType, int numOfPeoples) {
        List<Room> rooms = roomRepository.findWithoutReservationBetween(from, to);

        List<RoomSearchResult> allSolutions = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            final Room room = rooms.get(i);
            List<Room> parentRoomChain = Stream.of(room).collect(Collectors.toList());
            findCombinationOfRooms(rooms.subList(i + 1, rooms.size()), numOfPeoples, parentRoomChain, allSolutions);
        }

        allSolutions.forEach(solution -> {
            double price = reservationPriceCalculator.calculate(solution.getRooms(), from, to);
            solution.setFrom(from);
            solution.setTo(to);
            solution.setRoomsPrice(price);
        });

        allSolutions = allSolutions.stream().sorted((result1, result2) -> {
            final int fitness1 = calculateFitness(result1, preferredType, numOfPeoples);
            final int fitness2 = calculateFitness(result2, preferredType, numOfPeoples);
            return fitness1 - fitness2;
        }).collect(Collectors.toList());
        log.info("allSolutions: {}", allSolutions.size());
        return allSolutions;
    }

    public void reserve(ReservationRequest request) {
        Optional<Client> clientOptional = clientService.getClientByEmail(request.getEmail());
        Client client;
        if (clientOptional.isPresent()) {
            client = clientOptional.get();
            if (!client.getFirstName().equalsIgnoreCase(request.getFirstName()) || !client.getLastName().equalsIgnoreCase(request.getLastName())) {
                throw new IllegalArgumentException("invalid user: " + request.getEmail());
            }
        } else {
            client = clientService.register(request.getEmail(), request.getFirstName(), request.getLastName(), pinGenerator.generate());
        }
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        List<Room> rooms = roomRepository.findByIdIn(request.getRoomIds());
        reservation.setRooms(rooms);
        reservation.setFromDay(new java.sql.Date(request.getFrom().getTime()));
        reservation.setToDay(new java.sql.Date(request.getTo().getTime()));
        double price = reservationPriceCalculator.calculate(rooms, request.getFrom(), request.getTo());
        reservation.setRoomPrice(price);
        //todo: get authenticated user, if its receptionist then set to reservation
        reservationRepository.save(reservation);
    }

    private void findCombinationOfRooms(List<Room> rooms, int minimumSize, List<Room> roomChain, List<RoomSearchResult> allSolutions) {
        if (rooms.isEmpty()) {
            return;
        }
        Integer roomChainSize = roomChain.stream().map(Room::getSize).reduce((i, j) -> i + j).get();
        if (roomChainSize >= minimumSize) {
            allSolutions.add(new RoomSearchResult(roomChain));
            return;
        }

        final Room edge = rooms.get(0);
        final List<Room> extendedRoomChain = new ArrayList<>(roomChain);
        extendedRoomChain.add(edge);

        Integer sumSize = extendedRoomChain.stream().map(Room::getSize).reduce((i, j) -> i + j).get();
        if (sumSize >= minimumSize) {
            allSolutions.add(new RoomSearchResult(extendedRoomChain));
        } else {
            roomChain = extendedRoomChain;
        }
        for (int i = 1; i < rooms.size(); i++) {
            final Room room = rooms.get(i);
            List<Room> newRoomChain = new ArrayList<>(roomChain);
            newRoomChain.add(room);
            sumSize = newRoomChain.stream().map(Room::getSize).reduce((k, j) -> k + j).get();
            if (sumSize < minimumSize) {
                findCombinationOfRooms(rooms.subList(i + 1, rooms.size()), minimumSize, newRoomChain, allSolutions);
            } else {
                allSolutions.add(new RoomSearchResult(newRoomChain));
            }
        }
    }

    private int calculateFitness(RoomSearchResult result, RoomType preferredType, int numOfPeoples) {
        int sizeFactor = result.getSize() - numOfPeoples == 0 ? 1000 : 50 - (result.getSize() - numOfPeoples);
//        int numOfRoomsFactor = result.getNumOfRooms() - preferredNumOfRooms == 0 ? 100 : 20 - Math.abs(result.getNumOfRooms() - preferredNumOfRooms);
        int preferredTypeFactor = (int) (20 *  result.getRooms().stream().filter(room -> room.getType() == preferredType).count());
        return (int) (sizeFactor + 100 * result.getRoomsPrice() + preferredTypeFactor);
    }
}
