package com.dynamics.andrzej.smart.hotel.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ChangedRoomPricePeriod {

    @Id
    @GeneratedValue
    private long id;

    private Date from;

    private Date to;

    private double priceFactor;
}
