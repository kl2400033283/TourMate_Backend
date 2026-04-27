package com.klu.controller;

import com.klu.entity.Booking;
import com.klu.entity.Guide;
import com.klu.repository.BookingRepository;
import com.klu.repository.GuideRepository;
import com.klu.repository.UserRepository;
import com.klu.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    @Autowired
    private BookingService service;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private GuideRepository guideRepo;

    @Autowired
    private com.klu.repository.UserRepository userRepository;

    // Get bookings by email (looks up name from user table)
    @GetMapping("/user")
    public List<Booking> getBookingsByEmail(@RequestParam String email) {
        return service.getBookingsByEmail(email);
    }

    // Create booking
    @PostMapping
    public Booking create(@RequestBody Booking booking) {
        // If touristName not set, look up from email
        if ((booking.getTouristName() == null || booking.getTouristName().isEmpty()) && booking.getEmail() != null) {
            userRepository.findByEmail(booking.getEmail()).ifPresent(u -> booking.setTouristName(u.getName()));
        }
        // Save tourist email for chat key matching
        if (booking.getEmail() != null) {
            booking.setTouristEmail(booking.getEmail());
        }
        if (booking.getStatus() == null) booking.setStatus("pending");

        Booking saved = bookingRepo.save(booking);

        // If guide is assigned, also record in guide table
        if (booking.getGuideName() != null && !booking.getGuideName().isEmpty()) {
            boolean exists = !guideRepo.findByGuideName(booking.getGuideName()).isEmpty();
            if (!exists) {
                Guide g = new Guide();
                g.setGuideName(booking.getGuideName());
                g.setTouristName(booking.getTouristName());
                g.setCities(booking.getCities());
                g.setStatus("pending");
                g.setTotalBookings(1);
                guideRepo.save(g);
            }
        }

        return saved;
    }

    @GetMapping
    public List<Booking> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public Booking update(@PathVariable Long id, @RequestBody Booking booking) {
        return service.update(id, booking);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
