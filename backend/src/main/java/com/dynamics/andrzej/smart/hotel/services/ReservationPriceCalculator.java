package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.RoomSearchResult;
import com.dynamics.andrzej.smart.hotel.entities.ChangedPricePeriod;
import com.dynamics.andrzej.smart.hotel.entities.RoomType;
import com.dynamics.andrzej.smart.hotel.entities.SeasonPrice;

import java.util.List;

public class ReservationPriceCalculator {
    private final double priceForSingleRoom = 100;
    private final double factorForPremiumRoom = 0.2;
    private final RoomSearchResult roomSearchResult;
    private final double seasonPrices;
    private final double changedPricePeriods;

    public ReservationPriceCalculator(RoomSearchResult roomSearchResult, List<SeasonPrice> seasonPrices, List<ChangedPricePeriod> changedPricePeriods) {
        this.roomSearchResult = roomSearchResult;
        this.seasonPrices = seasonPrices.stream()
                .map(SeasonPrice::getPriceFactor)
                .reduce((aDouble, bDouble) -> aDouble + bDouble)
                .orElse(0.0);
        this.changedPricePeriods = changedPricePeriods.stream()
                .map(ChangedPricePeriod::getPriceFactor)
                .reduce((aDouble, bDouble) -> aDouble + bDouble)
                .orElse(0.0);
    }

    public double calculate() {
        return roomSearchResult.getRooms().stream().map(room -> {
            double price = priceForSingleRoom * room.getSize();
            double typeFactor = 0;
            double seasonPriceFactor = price * seasonPrices;
            double changedPricePeriodFactor = price * changedPricePeriods;
            if (room.getType() == RoomType.PREMIUM) {
                typeFactor = price * 0.2;
            }
            return price + typeFactor + seasonPriceFactor + changedPricePeriodFactor;
        }).reduce((aDouble, bDouble) -> aDouble + bDouble).orElseThrow(() -> new IllegalArgumentException("Something went wrong"));
    }
}
