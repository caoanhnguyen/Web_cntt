package com.kma.services;

import com.kma.models.voteDTO;

public interface voteService {
    voteDTO getVotesOfDiscussion(Integer discussionId);

    voteDTO getVotesOfAnswer(Integer answerId);

    void voteDiscussion(Integer discussionId, String voteType);

    void voteAnswer(Integer answerId, String voteType);

    boolean isVoted(Integer discussionId, Integer answerId);
}
