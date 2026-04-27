package com.klu.repository;

import com.klu.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {
    List<Guide> findByGuideName(String guideName);
    List<Guide> findByTouristName(String touristName);
}
