package com.kma.repository;

import com.kma.enums.DiscussionStatus;
import com.kma.repository.entities.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface discussionRepo extends JpaRepository<Discussion, Integer> {

    List<Discussion> findTop6ByStatusOrderByDiscussionIdDesc(DiscussionStatus status);

    Page<Discussion> findByStatus(DiscussionStatus status, Pageable pageable);

    //JPQL
    @Query("SELECT DISTINCT dis FROM Discussion dis " +
            "LEFT JOIN dis.tags tagList " +
            "WHERE (:title IS NULL OR dis.title LIKE %:title%) " +
            "AND (dis.status = com.kma.enums.DiscussionStatus.APPROVED) " +
            "AND (:tagName IS NULL OR tagList.name LIKE %:tagName%) " +
            "ORDER BY dis.discussionId DESC")
    Page<Discussion> findByAllCondition(@Param("title") String title,
                                        @Param("tagName") String tagName,
                                        Pageable pageable);

}
