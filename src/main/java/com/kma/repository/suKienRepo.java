package com.kma.repository;

import com.kma.repository.entities.SuKien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface suKienRepo extends JpaRepository<SuKien, Integer> {
    //JPQL
    @Query("SELECT sk FROM SuKien sk " +
            "WHERE sk.eventName LIKE %:eventName% " +
            "AND sk.location LIKE %:location% " +
            "AND sk.organizedBy LIKE %:organizedBy% " +
            "ORDER BY sk.eventId DESC")
    Page<SuKien> findByAllCondition(@Param("eventName") String eventName, @Param("location") String location, @Param("organizedBy") String organizedBy, Pageable pageabl);
}
