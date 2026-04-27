package com.klu.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity; // MISSING IMPORT
import java.util.*;

@RestController
@RequestMapping("/api/cities")
@CrossOrigin(origins = "https://tour-mate-frontend.vercel.app")
public class CityController {

    @GetMapping("/{cityName}")
    public ResponseEntity<?> getCityDetails(@PathVariable String cityName) {
        // Correcting the Map declaration (fixed the typo from your screenshot)
        Map<String, Object> cityData = new HashMap<>();
        
        if (cityName.equalsIgnoreCase("mumbai")) {
            cityData.put("name", "Mumbai");
            cityData.put("description", "The City of Dreams");
            return ResponseEntity.ok(cityData);
        } else if (cityName.equalsIgnoreCase("warangal")) {
            cityData.put("name", "Warangal");
            cityData.put("description", "Historical City of Kakatiyas");
            return ResponseEntity.ok(cityData);
        } else if (cityName.equalsIgnoreCase("bangalore")) {
            cityData.put("name", "Bangalore");
            cityData.put("description", "Silicon Valley of India");
            return ResponseEntity.ok(cityData);
        }
        
        // This ensures the frontend receives a JSON object even for errors
        Map<String, String> error = new HashMap<>();
        error.put("message", "City data not available in database");
        return ResponseEntity.status(404).body(error);
    }
}