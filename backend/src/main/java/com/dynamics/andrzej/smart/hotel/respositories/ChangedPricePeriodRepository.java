package com.dynamics.andrzej.smart.hotel.respositories;

import com.dynamics.andrzej.smart.hotel.entities.ChangedPricePeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ChangedPricePeriodRepository extends JpaRepository<ChangedPricePeriod, Long> {

    List<ChangedPricePeriod> findAllByFromDayBeforeAndToDayAfter(Date from, Date to);
}
