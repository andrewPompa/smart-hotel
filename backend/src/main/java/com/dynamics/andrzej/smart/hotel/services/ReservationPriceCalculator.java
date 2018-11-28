package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.entities.ChangedPricePeriod;
import com.dynamics.andrzej.smart.hotel.entities.Room;
import com.dynamics.andrzej.smart.hotel.entities.RoomType;
import com.dynamics.andrzej.smart.hotel.entities.SeasonPrice;
import com.dynamics.andrzej.smart.hotel.respositories.ChangedPricePeriodRepository;
import com.dynamics.andrzej.smart.hotel.respositories.SeasonPriceRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ReservationPriceCalculator {
    private final double priceForSingleRoom = 100;
    private final double factorForPremiumRoom = 0.4;
    private final SeasonPriceRepository seasonPriceRepository;
    private final ChangedPricePeriodRepository changedPricePeriodRepository;

    public ReservationPriceCalculator(SeasonPriceRepository seasonPriceRepository, ChangedPricePeriodRepository changedPricePeriodRepository) {
        this.seasonPriceRepository = seasonPriceRepository;
        this.changedPricePeriodRepository = changedPricePeriodRepository;
    }

    public double calculate(List<Room> rooms, Date from, Date to) {
        double changePricePeriodFactor = calculateChangePricePeriodFactor(from, to);
        double seasonPricesPeriodFactor = calculateSeasonPricesPeriodFactor(from, to);

        return rooms.stream().map(room -> {
            double price = priceForSingleRoom * room.getSize();
            double typeFactor = 0;
            double seasonPriceFactor = price * seasonPricesPeriodFactor;
            double changedPricePeriodFactor = price * changePricePeriodFactor;
            if (room.getType() == RoomType.PREMIUM) {
                typeFactor = price * factorForPremiumRoom;
            }
            return price + typeFactor + seasonPriceFactor + changedPricePeriodFactor;
        }).reduce((aDouble, bDouble) -> aDouble + bDouble).orElseThrow(() -> new IllegalArgumentException("Something went wrong"));
    }

    private double calculateSeasonPricesPeriodFactor(Date from, Date to) {
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

        return seasonPrices.stream()
                .map(SeasonPrice::getPriceFactor)
                .reduce((aDouble, bDouble) -> aDouble + bDouble)
                .orElse(0.0);
    }

    private double calculateChangePricePeriodFactor(Date from, Date to) {
        final List<ChangedPricePeriod> changedPricePeriods = changedPricePeriodRepository.findAllByFromDayBeforeAndToDayAfter(from, to);
        return changedPricePeriods.stream()
                .map(ChangedPricePeriod::getPriceFactor)
                .reduce((aDouble, bDouble) -> aDouble + bDouble)
                .orElse(0.0);
    }
}
