package com.kma.repository;

import com.kma.repository.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface voteRepo extends JpaRepository<Vote, Integer> {

    Optional<Vote> findByUser_UserIdAndDiscussion_DiscussionId(Integer userId, Integer discussionId);

    Optional<Vote> findByUser_UserIdAndAnswer_AnswerId(Integer userId, Integer answerId);

    boolean existsByUser_UserIdAndDiscussion_DiscussionId(Integer userId, Integer discussionId);

    boolean existsByUser_UserIdAndAnswer_AnswerId(Integer userId, Integer answerId);



    @Query(value = "SELECT " +
            "SUM(CASE WHEN type = 'UP' THEN 1 ELSE 0 END) AS upvote_count, " +
            "SUM(CASE WHEN type = 'DOWN' THEN 1 ELSE 0 END) AS downvote_count " +
            "FROM vote WHERE discussionId = :discussionId", nativeQuery = true)
    Object[] getVotesOfDiscussion(@Param("discussionId") Integer discussionId);

    @Query(value = "SELECT " +
            "SUM(CASE WHEN type = 'UP' THEN 1 ELSE 0 END) AS upvote_count, " +
            "SUM(CASE WHEN type = 'DOWN' THEN 1 ELSE 0 END) AS downvote_count " +
            "FROM vote WHERE answerId = :answerId", nativeQuery = true)
    Object[] getVotesOfAnswer(@Param("answerId") Integer answerId);

}
