package com.kma.repository;

import com.kma.repository.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface tagRepo extends JpaRepository<Tag, Integer> {

    List<Tag> findAllByOrderByCategory();

    boolean existsByName(String tagName);

}
