package com.dynamics.andrzej.smart.hotel.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ChangedPricePeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Date fromDay;

    private Date toDay;

    private double priceFactor;
}
