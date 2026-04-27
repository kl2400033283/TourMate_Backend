package com.klu.controller;

import com.klu.entity.Booking;
import com.klu.entity.Guide;
import com.klu.repository.BookingRepository;
import com.klu.repository.GuideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guide")
@CrossOrigin(origins = "https://tour-mate-frontend.vercel.app")
public class GuideController {

    @Autowired
    private GuideRepository guideRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private com.klu.repository.UserRepository userRepo;

    // Guide dashboard — returns bookings assigned to this specific guide by email
    @GetMapping("/dashboard")
    public List<Booking> getDashboard(@RequestParam(required = false) String guideName) {
        return bookingRepo.findAll().stream()
                .filter(b -> b.getGuideName() != null && !b.getGuideName().isEmpty())
                .filter(b -> guideName == null || b.getGuideName().equalsIgnoreCase(guideName))
                .toList();
    }

    // Update booking status (accept/reject) by guide
    @PutMapping("/tour/{id}")
    public Booking updateTourStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return bookingRepo.findById(id).map(b -> {
            b.setStatus(body.get("status"));
            // Update guide stats
            guideRepo.findByGuideName(b.getGuideName()).stream().findFirst().ifPresent(g -> {
                String status = body.get("status");
                if ("confirmed".equalsIgnoreCase(status)) g.setAccepted(g.getAccepted() + 1);
                else if ("rejected".equalsIgnoreCase(status)) g.setRejected(g.getRejected() + 1);
                g.setTotalBookings(g.getAccepted() + g.getRejected());
                guideRepo.save(g);
            });
            return bookingRepo.save(b);
        }).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    // Register a guide
    @PostMapping("/register")
    public Guide register(@RequestBody Guide g) {
        g.setStatus("PENDING");
        return guideRepo.save(g);
    }

    // Toggle guide availability (free/busy)
    @PutMapping("/availability/{guideName}")
    public Guide toggleAvailability(@PathVariable String guideName, @RequestBody Map<String, Boolean> body) {
        return guideRepo.findByGuideName(guideName).stream().findFirst().map(g -> {
            g.setAvailable(body.get("available"));
            return guideRepo.save(g);
        }).orElseThrow(() -> new RuntimeException("Guide not found"));
    }

    // Get all guides
    @GetMapping
    public List<Guide> getAll() {
        return guideRepo.findAll();
    }
}
