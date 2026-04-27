package com.klu.controller;

import com.klu.entity.Booking;
import com.klu.entity.Homestay;
import com.klu.entity.User;
import com.klu.repository.BookingRepository;
import com.klu.repository.HomestayRepository;
import com.klu.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private HomestayRepository homeRepo;

    @Autowired
    private BookingRepository bookingRepo;

    // GET ALL USERS (without passwords)
    @GetMapping("/users")
    public List<Map<String, Object>> getUsers() {
        return userRepo.findAll().stream().map(u -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", u.getId());
            m.put("name", u.getName());
            m.put("email", u.getEmail());
            m.put("role", u.getRole());
            m.put("approved", u.isApproved());
            m.put("city", u.getCity());
            return m;
        }).toList();
    }

    // GET ALL BOOKINGS
    @GetMapping("/bookings")
    public List<Booking> getBookings() {
        return bookingRepo.findAll();
    }

    // GET ALL APPROVED HOMESTAYS for tourist booking page
    @GetMapping("/approved-homestays")
    public List<Homestay> getApprovedHomestays() {
        return homeRepo.findByStatus("APPROVED");
    }

    // GET APPROVED GUIDES filtered by city
    @GetMapping("/approved-guides")
    public List<Map<String, Object>> getApprovedGuides(@RequestParam(required = false) String city) {
        return userRepo.findAll().stream()
                .filter(u -> "guide".equalsIgnoreCase(u.getRole()) && u.isApproved())
                .filter(u -> city == null || city.equalsIgnoreCase(u.getCity()))
                .map(u -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", u.getId());
                    m.put("name", u.getName());
                    m.put("email", u.getEmail());
                    m.put("city", u.getCity() != null ? u.getCity() : "");
                    return m;
                })
                .toList();
    }

    // GUIDE SETS THEIR WORKING CITY by ID
    @PutMapping("/guide/city/{id}")
    public User updateGuideCity(@PathVariable Long id, @RequestBody Map<String, String> body) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setCity(body.get("city"));
        return userRepo.save(user);
    }

    // GUIDE SETS THEIR WORKING CITY by email (fallback)
    @PutMapping("/guide/city/email")
    public User updateGuideCityByEmail(@RequestBody Map<String, String> body) {
        User user = userRepo.findByEmail(body.get("email"))
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setCity(body.get("city"));
        return userRepo.save(user);
    }

    // GET ALL GUIDES (users with role=guide)
    @GetMapping("/guides")
    public List<User> getGuides() {
        return userRepo.findAll().stream()
                .filter(u -> "guide".equalsIgnoreCase(u.getRole()))
                .toList();
    }

    // GET ALL PROPERTIES (Homestays)
    @GetMapping("/properties")
    public List<Homestay> getProperties() {
        return homeRepo.findAll();
    }

    // APPROVE / REJECT USER — updates approved field in DB
    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody Map<String, String> body) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String status = body.get("status");
        user.setApproved("approved".equalsIgnoreCase(status));
        return userRepo.save(user);
    }

    // DELETE USER — removes from DB
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (!userRepo.existsById(id)) throw new RuntimeException("User not found");
        userRepo.deleteById(id);
        return "User deleted successfully";
    }

    // UPDATE PROPERTY STATUS
    @PutMapping("/property/{id}")
    public Homestay updateProperty(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Homestay home = homeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Homestay not found"));
        home.setStatus(body.get("status"));
        return homeRepo.save(home);
    }
}
