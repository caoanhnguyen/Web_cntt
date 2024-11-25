package com.kma.repository;

import com.kma.repository.entities.Lop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface lopRepo extends JpaRepository<Lop, Integer> {
    Lop findByTenLop(String tenLop);
}
