package com.kma.repository;

import com.kma.repository.entities.PhongBan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface phongBanRepo extends JpaRepository<PhongBan, String> {
    Page<PhongBan> findByMaPhongBanContainingAndTenPhongBanContaining(String maPhongBan, String tenPhongBan, Pageable pageable);

}
