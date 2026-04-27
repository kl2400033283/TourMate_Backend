package com.klu.controller;

import com.klu.entity.Booking;
import com.klu.entity.Homestay;
import com.klu.repository.BookingRepository;
import com.klu.repository.HomestayRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/host")
@CrossOrigin(origins = "http://localhost:5173")
public class HostController {

    @Autowired
    private HomestayRepository repo;

    @Autowired
    private BookingRepository bookingRepo;

    // GET HOST DASHBOARD - bookings matching this host's approved properties by email
    @GetMapping("/dashboard")
    public List<Booking> getDashboard(@RequestParam(required = false) String hostEmail) {
        List<String> hostPropertyNames = repo.findAll().stream()
                .filter(h -> "APPROVED".equals(h.getStatus()))
                .filter(h -> hostEmail == null || hostEmail.equalsIgnoreCase(h.getHostEmail()))
                .map(Homestay::getHomestayName)
                .toList();

        if (hostPropertyNames.isEmpty()) return List.of();

        return bookingRepo.findAll().stream()
                .filter(b -> b.getHomestayName() != null && !b.getHomestayName().isEmpty())
                .filter(b -> hostPropertyNames.contains(b.getHomestayName()))
                .toList();
    }

    // UPDATE BOOKING STATUS + update touristName in homestay table
    @PutMapping("/booking/{id}")
    public Booking updateBooking(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return bookingRepo.findById(id).map(b -> {
            String status = body.get("status");
            b.setStatus(status);
            // When confirmed, record tourist name in homestay table
            if ("confirmed".equalsIgnoreCase(status) && b.getHomestayName() != null) {
                repo.findAll().stream()
                    .filter(h -> b.getHomestayName().equals(h.getHomestayName()))
                    .findFirst().ifPresent(h -> {
                        h.setTouristName(b.getTouristName());
                        repo.save(h);
                    });
            }
            return bookingRepo.save(b);
        }).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    // CREATE HOMESTAY PROPERTY
    @PostMapping
    public Homestay create(@RequestBody Homestay h) {
        h.setStatus("PENDING");
        return repo.save(h);
    }

    // GET ALL HOMESTAY PROPERTIES (filtered by hostEmail if provided)
    @GetMapping
    public List<Homestay> getAll(@RequestParam(required = false) String hostEmail) {
        if (hostEmail != null && !hostEmail.isEmpty()) {
            return repo.findAll().stream()
                    .filter(h -> hostEmail.equalsIgnoreCase(h.getHostEmail()))
                    .toList();
        }
        return repo.findAll();
    }

    // UPDATE HOMESTAY PROPERTY STATUS
    @PutMapping("/{id}")
    public Homestay update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return repo.findById(id).map(h -> {
            if (body.containsKey("status")) h.setStatus(body.get("status"));
            if (body.containsKey("homestayName")) h.setHomestayName(body.get("homestayName"));
            if (body.containsKey("hostName")) h.setHostName(body.get("hostName"));
            return repo.save(h);
        }).orElseThrow(() -> new RuntimeException("Homestay not found"));
    }

    // DELETE HOMESTAY PROPERTY
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "Deleted";
    }
}
