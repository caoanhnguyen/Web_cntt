package com.kma.repository;

import com.kma.repository.entities.DangKySuKien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface dkSuKienRepo extends JpaRepository<DangKySuKien, Integer> {

    @Query("SELECT d FROM DangKySuKien d JOIN FETCH d.sinhVien s WHERE d.event.eventId = :eventId")
    List<DangKySuKien> findByEventId(@Param("eventId") Integer eventId);

}
