package com.kma.models;

import com.kma.enums.VoteType;

public class voteDTO {
    private Long upVotes;
    private Long downVotes;
    private VoteType vote;

    public voteDTO(Long upVotes, Long downVotes, VoteType vote) {
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.vote = vote;
    }

    public voteDTO() {
    }

    public Long getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(Long upVotes) {
        this.upVotes = upVotes;
    }

    public Long getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(Long downVotes) {
        this.downVotes = downVotes;
    }

    public VoteType getVote() {
        return vote;
    }

    public void setVote(VoteType vote) {
        this.vote = vote;
    }
}
