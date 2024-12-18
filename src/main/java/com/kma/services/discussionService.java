package com.kma.services;

import com.kma.models.discussionDTO;
import com.kma.models.discussionRequestDTO;
import com.kma.models.discussionResponseDTO;
import com.kma.models.paginationResponseDTO;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface discussionService {
    discussionDTO getById(Integer discussionId);

    paginationResponseDTO<discussionResponseDTO> getAllDiscussion(Map<String,Object> params, Integer page, Integer size);

    paginationResponseDTO<discussionResponseDTO> getAllPendingDiscuss(Integer page, Integer size);

    List<discussionResponseDTO> getLatestDiscussions();

    void addDiscussion(discussionRequestDTO discussReqDTO, List<Integer> tagIdList, Principal principal);

    void updateDiscussion(Integer discussionId, discussionRequestDTO discussReqDTO, List<Integer> tagIdList);

    void updateDiscussionStatus(Integer discussionId, String discussionStatus);

    void deleteDiscussion(Integer discussionId);

}
