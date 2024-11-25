package com.kma.repository;

import com.kma.repository.entities.SinhVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface sinhVienRepo extends JpaRepository<SinhVien, String> {

    SinhVien findByAvaFileCode(String avaFileCode);

    //JPQL
    @Query("SELECT sv FROM SinhVien sv JOIN sv.lop l " +
            "WHERE sv.maSinhVien LIKE %:maSinhVien% " +
            "AND sv.tenSinhVien LIKE %:tenSinhVien% " +
            "AND l.tenLop LIKE %:tenLop% " +
            "ORDER BY sv.maSinhVien ASC")
    Page<SinhVien> findByAllCondition(@Param("tenSinhVien") String tenSinhVien, @Param("maSinhVien") String maSinhVien, @Param("tenLop") String tenLop, Pageable pageabl);
}
