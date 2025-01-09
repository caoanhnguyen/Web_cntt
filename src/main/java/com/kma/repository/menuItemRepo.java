package com.kma.repository;

import com.kma.repository.entities.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface menuItemRepo extends JpaRepository<MenuItem, Integer> {

    @Query("SELECT m FROM MenuItem m WHERE m.parentId IS NULL AND m.isDeleted = false")
    List<MenuItem> findRootMenuItems();

    @Query("SELECT m FROM MenuItem m " +
            "WHERE m.parentId IS NULL " +
            "AND (:title IS NULL OR m.title LIKE %:title%) " +
            "AND (:isDeleted IS NULL OR m.isDeleted = :isDeleted) ")
    List<MenuItem> findRootMenuItemsForAdmin(@Param("title") String title, @Param("isDeleted") Boolean isDeleted);

    @Query("SELECT m FROM MenuItem m WHERE m.parentId = :parentId")
    List<MenuItem> findChildren(@Param("parentId") Integer parentId);

}
