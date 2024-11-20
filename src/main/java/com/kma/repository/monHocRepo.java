package com.kma.repository;

import com.kma.repository.entities.MonHoc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface monHocRepo extends JpaRepository<MonHoc, Integer> {

    MonHoc findByTenMonHocContaining(String tenMonHoc);
}
