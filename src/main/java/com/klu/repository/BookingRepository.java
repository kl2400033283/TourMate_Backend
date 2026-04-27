package com.klu.repository;

import com.klu.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTouristName(String touristName);
    List<Booking> findByTouristEmail(String touristEmail);
    List<Booking> findByStatus(String status);
}
