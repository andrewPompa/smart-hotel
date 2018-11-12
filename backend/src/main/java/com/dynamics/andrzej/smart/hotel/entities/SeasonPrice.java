package com.dynamics.andrzej.smart.hotel.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class SeasonPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int fromDay;

    private int fromMonth;

    private int toDay;

    private int toMonth;

    private double priceFactor;
}
