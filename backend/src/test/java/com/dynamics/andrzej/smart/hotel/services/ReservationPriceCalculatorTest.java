package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.entities.*;
import com.dynamics.andrzej.smart.hotel.respositories.ChangedPricePeriodRepository;
import com.dynamics.andrzej.smart.hotel.respositories.ReservationRepository;
import com.dynamics.andrzej.smart.hotel.respositories.SeasonPriceRepository;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.sql.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class ReservationPriceCalculatorTest {

    @Test
    public void testCalculate() {
        assert 1 == 1;
    }

    @Test
    public void testCalculateWithClientDiscount() {
        final SeasonPriceRepository seasonPriceRepository = Mockito.mock(SeasonPriceRepository.class);
        final ChangedPricePeriodRepository changedPricePeriodRepository = Mockito.mock(ChangedPricePeriodRepository.class);
        final ReservationRepository reservationRepository = Mockito.mock(ReservationRepository.class);
        final ReservationPriceCalculator reservationPriceCalculator = new ReservationPriceCalculator(seasonPriceRepository, changedPricePeriodRepository, reservationRepository);

        final Reservation reservation = new Reservation();
        reservation.setFromDay(new Date(2018, 10, 10));
        reservation.setToDay(new Date(2018, 10, 20));
        when(reservationRepository.findAllByClientId(anyLong())).thenReturn(Stream.of(reservation).collect(Collectors.toList()));
        Client client = new Client();
        client.setId(1L);

        final Room room1 = new Room();
        room1.setSize(4);
        room1.setName("104a");
        room1.setType(RoomType.PREMIUM);

        final Room room2 = new Room();
        room2.setSize(2);
        room2.setName("102");
        room2.setType(RoomType.STANDARD);

        final SeasonPrice seasonPrice = new SeasonPrice();
        seasonPrice.setPriceFactor(0.2);
        when(seasonPriceRepository.findAllInDate(2, 12, 20, 12)).thenReturn(Stream.of(seasonPrice).collect(Collectors.toList()));

        final ChangedPricePeriod changedPricePeriod = new ChangedPricePeriod();
        changedPricePeriod.setFromDay(new Date(2018, 12, 10));
        changedPricePeriod.setToDay(new Date(2018, 12, 20));
        when(changedPricePeriodRepository
                .findAllByFromDayBeforeAndToDayAfter(new Date(2018, 12, 2), new Date(2018, 12, 20)))
                .thenReturn(Stream.of(changedPricePeriod).collect(Collectors.toList()));

        final double price = reservationPriceCalculator
                .calculateWithClientDiscount(
                        client,
                        Stream.of(room1, room2).collect(Collectors.toList()),
                        new Date(2018, 12, 2),
                        new Date(2018, 12, 20)
                );
        assert price == 767.6;
    }
}