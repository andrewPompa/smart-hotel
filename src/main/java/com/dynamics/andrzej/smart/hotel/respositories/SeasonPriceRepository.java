package com.dynamics.andrzej.smart.hotel.respositories;

import com.dynamics.andrzej.smart.hotel.entities.ChangedPricePeriod;
import com.dynamics.andrzej.smart.hotel.entities.Receptionist;
import com.dynamics.andrzej.smart.hotel.entities.SeasonPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonPriceRepository extends JpaRepository<SeasonPrice, Long> {

    @Query("select s from SeasonPrice s where s.fromDay <= ?1 and s.fromMonth <= ?2 and s.toDay >= ?3 and s.toMonth < ?4")
    List<SeasonPrice> findAllInDate(int fromDay, int fromMonth, int toDay, int toMonth);
}
