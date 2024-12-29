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

import java.math.BigDecimal;
import java.sql.Date;
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

    public discussionDTO convertToDiscussDTO(Discussion discussion, String userName){
        discussionDTO dto = modelMapper.map(discussion, discussionDTO.class);

        // Get info
        User user = discussion.getUser();
        userDTO userDTO = userUtil.getInfoOfUser(user);
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

        dto.setOwner(userName.equals(user.getUserName()));

        return dto;
    }

    public discussionResponseDTO convertToDiscussResDTO(Object[] row) {
        // Tạo đối tượng DTO mới
        discussionResponseDTO discussResDTO = new discussionResponseDTO();

        // Gán các giá trị từ Object[] vào DTO
        discussResDTO.setDiscussionId((Integer) row[0]); // discussionId
        discussResDTO.setTitle((String) row[1]); // title
        discussResDTO.setContent((String) row[2]); // content
        discussResDTO.setCreateAt((Date) row[3]); // createAt

        // Gán thông tin trạng thái thảo luận
        discussResDTO.setDiscussionStatus((String) row[4]); // status (nếu có)

        // Gán thông tin người đăng
        discussResDTO.setAuthor_DTO(new userDTO(
                row[5],
                (String) row[6],
                "/downloadProfile/" + row[7]
        ));

        // Gán điểm của bài thảo luận
        discussResDTO.setScore((BigDecimal) row[8]); // score

        // Tính điểm và số lượng upvote, downvote, answers từ Object[]
        discussResDTO.setVoteDTO(new voteDTO(
                (Long) row[9], // upVote count
                (Long) row[10], // downVote count
                null
        ));

        discussResDTO.setAnswerQuantity((Long) row[11]); // answer count

        return discussResDTO;
    }


    public void convertToDiscussion(discussionRequestDTO discussReqDTO, Discussion discussion, List<Integer> tagIdList) {
        // Map các trường thông thường từ DTO sang Entity
        modelMapper.map(discussReqDTO, discussion);

        // Khởi tạo Set<Tag>
        Set<Tag> tags = new HashSet<>();

        // Lặp qua danh sách tagId và thêm Tag vào Set
        for (Integer tagId : tagIdList) {
            tRepo.findById(tagId).ifPresent(tags::add);
        }

        // Set tags vào discussion
        discussion.setTags(tags);

    }

}
