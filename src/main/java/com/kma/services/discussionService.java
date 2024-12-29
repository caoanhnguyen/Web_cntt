package com.kma.services;

import com.kma.models.discussionDTO;
import com.kma.models.discussionRequestDTO;
import com.kma.models.discussionResponseDTO;
import com.kma.models.paginationResponseDTO;

import java.util.List;
import java.util.Map;

public interface discussionService {
    paginationResponseDTO<discussionResponseDTO> getAllDiscussionByTag(Integer tagId, Integer page, Integer size, String sort, String order);

    discussionDTO getById(Integer discussionId);

    paginationResponseDTO<discussionResponseDTO> getAllDiscussionOfUser(Map<String,Object> params, Integer page, Integer size, String sort, String order);

    paginationResponseDTO<discussionResponseDTO> getAllDiscussion(Map<String,Object> params, Integer page, Integer size, String sort, String order);

    paginationResponseDTO<discussionResponseDTO> getAllPendingDiscuss(Integer page, Integer size);

    void addDiscussion(discussionRequestDTO discussReqDTO, List<Integer> tagIdList);

    void updateDiscussion(Integer discussionId, discussionRequestDTO discussReqDTO, List<Integer> tagIdList);

    void updateDiscussionStatus(List<Integer> discussionIds, String discussionStatus);

    void deleteDiscussion(Integer discussionId);

    boolean isOwner(Integer discussionId, Integer userId);

}
