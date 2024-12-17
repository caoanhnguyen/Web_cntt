package com.kma.repository;

import com.kma.repository.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface voteRepo extends JpaRepository<Vote, Integer> {

    @Query(value = "SELECT " +
            "SUM(CASE WHEN type = 'UP' THEN 1 ELSE 0 END) AS upvote_count, " +
            "SUM(CASE WHEN type = 'DOWN' THEN 1 ELSE 0 END) AS downvote_count " +
            "FROM vote WHERE discussionId = :discussionId", nativeQuery = true)
    Object[] getVotesOfDiscussion(@Param("discussionId") Integer discussionId);

}
