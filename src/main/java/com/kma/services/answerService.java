package com.kma.services;

import com.kma.models.answerDTO;
import com.kma.models.answerRequestDTO;
import com.kma.models.paginationResponseDTO;


public interface answerService {

    paginationResponseDTO<answerDTO> getAllAnswerOfDiscussion(Integer discussionId, Integer page, Integer size);

    void addAnswer(Integer discussionId, answerRequestDTO ansReqDTO);

    void updateAnswer(Integer discussionId, Integer answerId, answerRequestDTO ansReqDTO);

    void deleteAnswer(Integer discussionId, Integer answerId);

    boolean isOwner(Integer answerId, Integer userId);
}
