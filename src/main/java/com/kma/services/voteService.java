package com.kma.services;

import com.kma.models.voteDTO;

public interface voteService {
    voteDTO getVotesOfDiscussion(Integer discussionId);
}
