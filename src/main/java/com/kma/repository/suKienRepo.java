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
            "WHERE (sk.eventName LIKE %:eventName% OR :eventName IS NULL OR sk.eventName IS NULL)" +
            "AND (sk.location LIKE %:location% OR :location IS NULL OR sk.location IS NULL)" +
            "AND (sk.organizedBy LIKE %:organizedBy% OR :organizedBy IS NULL OR sk.organizedBy IS NULL)" +
            "ORDER BY sk.createAt DESC")
    Page<SuKien> findByAllCondition(@Param("eventName") String eventName, @Param("location") String location, @Param("organizedBy") String organizedBy, Pageable pageable);

    //SQL Native
    @Query(value = """
        SELECT\s
            sv.maSinhVien AS maSinhVien,
            sv.tenSinhVien AS tenSinhVien,
            sv.gioiTinh AS gioiTinh,
            sv.ngaySinh AS ngaySinh,
            sv.queQuan AS queQuan,
            sv.khoa AS khoa,
            l.tenLop AS tenLop
        FROM dang_ky_su_kien dksk
        INNER JOIN sinh_vien sv ON dksk.maSinhVien = sv.maSinhVien
        INNER JOIN lop l ON l.idLop = sv.idLop
        WHERE dksk.eventId = :eventId
        AND (:searchTerm IS NULL OR sv.maSinhVien LIKE CONCAT('%', :searchTerm, '%') OR sv.tenSinhVien LIKE CONCAT('%', :searchTerm, '%'))
        ORDER BY tenLop ASC, tenSinhVien ASC;
       \s""", nativeQuery = true)
    Page<Object[]> findSinhVienByEventId(@Param("searchTerm") String searchTerm, @Param("eventId") Integer eventId, Pageable pageable);
}
