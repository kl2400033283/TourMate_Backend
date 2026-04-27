package com.klu.dto;

import lombok.*;



import java.time.LocalDate;

@Data
public class BookingDTO {

    private String touristName;
    private String homestayName;
    private String guideName;

    private LocalDate startDate;
    private LocalDate endDate;

    private String cities;
    private String status;
}