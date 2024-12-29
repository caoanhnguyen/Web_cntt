package com.kma.repository;

import com.kma.repository.entities.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface answerRepo extends JpaRepository<Answer, Integer> {

    Page<Answer> findByDiscussion_DiscussionIdOrderByCreateAtDesc(Integer discussionId, Pageable pageable);

    Optional<Answer> findByAnswerIdAndDiscussion_DiscussionId(Integer answerId, Integer discussionId);

    boolean existsByAnswerIdAndUser_UserId(Integer answerId, Integer userId);

    @Query("SELECT COUNT(a) " +
            "FROM Answer a " +
            "WHERE a.discussion.discussionId = :discussionId ")
    Integer countAnswersByDiscussionId(@Param("discussionId") Integer discussionId);

}
