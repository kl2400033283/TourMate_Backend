package com.klu.repository;

import com.klu.entity.Homestay;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomestayRepository extends JpaRepository<Homestay, Long> {

    List<Homestay> findByStatus(String status);
}