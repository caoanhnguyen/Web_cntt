package com.kma.repository;

import com.kma.repository.entities.Slider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface sliderRepo extends JpaRepository<Slider, Integer> {
    @Query(value = "SELECT * " +
            "FROM slider a " +
            "ORDER BY a.createAt DESC, a.sliderId DESC",
            nativeQuery = true)
    Page<Slider> getAllSlider(Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM slider a " +
            "ORDER BY a.createAt DESC, a.sliderId DESC LIMIT 6",
            nativeQuery = true)
    List<Slider> findTop6ByOrderByCreateAtDesc();

}
