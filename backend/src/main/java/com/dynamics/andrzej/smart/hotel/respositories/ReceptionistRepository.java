package com.dynamics.andrzej.smart.hotel.respositories;

import com.dynamics.andrzej.smart.hotel.entities.Receptionist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceptionistRepository extends JpaRepository<Receptionist, Long> {
    Optional<Receptionist> findByLogin(String login);
}
