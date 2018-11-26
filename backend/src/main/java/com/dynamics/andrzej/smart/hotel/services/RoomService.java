package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.RoomSearchResult;
import com.dynamics.andrzej.smart.hotel.entities.*;
import com.dynamics.andrzej.smart.hotel.respositories.ChangedPricePeriodRepository;
import com.dynamics.andrzej.smart.hotel.respositories.ReservationRepository;
import com.dynamics.andrzej.smart.hotel.respositories.RoomRepository;
import com.dynamics.andrzej.smart.hotel.respositories.SeasonPriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ChangedPricePeriodRepository changedPricePeriodRepository;
    private final SeasonPriceRepository seasonPriceRepository;

    public RoomService(RoomRepository roomRepository, ReservationRepository reservationRepository, ChangedPricePeriodRepository changedPricePeriodRepository, SeasonPriceRepository seasonPriceRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.changedPricePeriodRepository = changedPricePeriodRepository;
        this.seasonPriceRepository = seasonPriceRepository;
    }

    public Room add(Room room) {
        final Optional<Room> byName = roomRepository.findByName(room.getName());
        if (byName.isPresent()) {
            throw new IllegalArgumentException("Room with this name already exists");
        }
        return roomRepository.save(room);
    }

    public void delete(Long id) {
        final List<Reservation> byRoomId = reservationRepository.findByRoomId(id);
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

        final Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(from);
        final Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(to);
        final List<SeasonPrice> seasonPrices = seasonPriceRepository.findAllInDate(
                fromCalendar.get(Calendar.DAY_OF_MONTH),
                fromCalendar.get(Calendar.MONTH),
                toCalendar.get(Calendar.DAY_OF_MONTH),
                toCalendar.get(Calendar.MONTH)
        );
        final List<ChangedPricePeriod> changedPricePeriods = changedPricePeriodRepository.findAllByFromDayBeforeAndToDayAfter(from, to);

        allSolutions.forEach(solution -> {
            final ReservationPriceCalculator reservationPriceCalculator = new ReservationPriceCalculator(solution, seasonPrices, changedPricePeriods);
            double price = reservationPriceCalculator.calculate();
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
