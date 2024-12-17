package com.kma.repository;

import com.kma.repository.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface tagRepo extends JpaRepository<Tag, Integer> {
}
