package com.kma.repository;

import com.kma.repository.entities.MonHoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface monHocRepo extends JpaRepository<MonHoc, Integer> {

    List<MonHoc> findAllByOrderByCategory();

    List<MonHoc> findByTenMonHocContaining(String tenMonHoc);

    List<MonHoc> findBySoTinChi(Integer soTinChi);

    List<MonHoc> findByTenMonHocContainingAndSoTinChi(String tenMonHoc, Integer soTinChi);
}
