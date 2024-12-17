package com.kma.repository;

import com.kma.repository.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface answerRepo extends JpaRepository<Answer, Integer> {

    @Query("SELECT COUNT(a) " +
            "FROM Answer a " +
            "WHERE a.discussion.discussionId = :discussionId")
    Integer countAnswersByDiscussionId(@Param("discussionId") Integer discussionId);

}
