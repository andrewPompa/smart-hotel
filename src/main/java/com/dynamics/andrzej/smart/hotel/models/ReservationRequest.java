package com.dynamics.andrzej.smart.hotel.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class ReservationRequest {

    private String email;
    private String firstName;
    private String lastName;
    private List<Long> roomIds;
    private Date from;
    private Date to;

}
