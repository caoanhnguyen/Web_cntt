package com.kma.converter;

import com.kma.models.*;
import com.kma.repository.entities.Discussion;
import com.kma.repository.entities.Tag;
import com.kma.repository.entities.User;
import com.kma.repository.tagRepo;
import com.kma.services.voteService;
import com.kma.utilities.userInfoUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class discussionDTOConverter {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    userInfoUtil userUtil;
    @Autowired
    tagDTOConverter tagConverter;
    @Autowired
    voteService voteServ;
    @Autowired
    tagRepo tRepo;

    public discussionDTO convertToDiscussDTO(Discussion discussion){
        discussionDTO dto = modelMapper.map(discussion, discussionDTO.class);

        // Get info
        User user = discussion.getUser();
        userDTO userDTO = userUtil.getUserInfo(user);
        dto.setAuthor(userDTO);
        
        // Get tagDTOList
        Set<Tag> tagList = discussion.getTags();
        List<tagDTO> tagDTOList = new ArrayList<>();
        for(Tag tag: tagList){
            tagDTOList.add(tagConverter.convertToTagDTO(tag));
        }
        tagDTOList.sort(Comparator.comparing(tagDTO::getTagId));
        dto.setTagDTOList(tagDTOList);

        // Get votes
        voteDTO voteDTO = voteServ.getVotesOfDiscussion(discussion.getDiscussionId());
        dto.setVoteDTO(voteDTO);

        // Get answer quantity
        Integer answers = discussion.getAnswers().size();
        dto.setAnswerQuantity(answers);

        return dto;
    }

    public discussionResponseDTO convertToDiscussResDTO(Discussion discussion){
        discussionResponseDTO discussResDTO = modelMapper.map(discussion, discussionResponseDTO.class);

        // Get author name
        User user = discussion.getUser();
        String authorName = userUtil.getUserInfo(user).getName();
        discussResDTO.setAuthorName(authorName);

        // Get votes
        voteDTO voteDTO = voteServ.getVotesOfDiscussion(discussion.getDiscussionId());
        discussResDTO.setVoteDTO(voteDTO);

        // Get answer quantity
        Integer answers = discussion.getAnswers().size();
        discussResDTO.setAnswerQuantity(answers);

        return discussResDTO;
    }

    public Discussion convertToDiscussion(discussionRequestDTO discussReqDTO, List<Integer> tagIdList) {
        // Map các trường thông thường từ DTO sang Entity
        Discussion discussion = modelMapper.map(discussReqDTO, Discussion.class);

        // Khởi tạo Set<Tag>
        Set<Tag> tags = new HashSet<>();

        // Lặp qua danh sách tagId và thêm Tag vào Set
        for (Integer tagId : tagIdList) {
            tRepo.findById(tagId).ifPresent(tags::add);
        }

        // Set tags vào discussion
        discussion.setTags(tags);

        return discussion;
    }

}
