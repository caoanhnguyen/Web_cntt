package com.kma.repository;

import com.kma.repository.entities.CV;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CVRepo extends JpaRepository<CV, Integer> {

    boolean existsByCVIdAndNhanVien_idUser(Integer CVId, Integer idUser);
}
