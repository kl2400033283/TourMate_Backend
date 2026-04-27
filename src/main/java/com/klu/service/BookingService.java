package com.klu.service;

import com.klu.entity.Booking;
import com.klu.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository repository;

    @Autowired
    private com.klu.repository.UserRepository userRepository;

    public List<Booking> getBookingsByEmail(String email) {
        return repository.findByTouristEmail(email);
    }

    public Booking create(Booking booking) {
        if (booking.getStatus() == null) booking.setStatus("pending");
        return repository.save(booking);
    }

    public List<Booking> getAll() {
        return repository.findAll();
    }

    public Booking update(Long id, Booking updated) {
        return repository.findById(id).map(b -> {
            if (updated.getStatus() != null) b.setStatus(updated.getStatus());
            if (updated.getHomestayName() != null) b.setHomestayName(updated.getHomestayName());
            if (updated.getGuideName() != null) b.setGuideName(updated.getGuideName());
            if (updated.getTouristName() != null) b.setTouristName(updated.getTouristName());
            if (updated.getCities() != null) b.setCities(updated.getCities());
            if (updated.getStartDate() != null) b.setStartDate(updated.getStartDate());
            if (updated.getEndDate() != null) b.setEndDate(updated.getEndDate());
            return repository.save(b);
        }).orElseThrow(() -> new RuntimeException("Booking not found with id " + id));
    }

    public String delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Booking deleted successfully";
        }
        return "Booking not found";
    }
}
