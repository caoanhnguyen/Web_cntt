package com.kma.converter;

import com.kma.models.answerDTO;
import com.kma.models.answerRequestDTO;
import com.kma.models.userDTO;
import com.kma.models.voteDTO;
import com.kma.repository.discussionRepo;
import com.kma.repository.entities.Answer;
import com.kma.repository.entities.Discussion;
import com.kma.repository.entities.User;
import com.kma.services.voteService;
import com.kma.utilities.userInfoUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class answerDTOConverter {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    userInfoUtil userUtil;
    @Autowired
    voteService voteServ;
    @Autowired
    discussionRepo discussRepo;

    public answerDTO convertToAnswerDTO(Answer answer, String userName){
        answerDTO ansDTO = modelMapper.map(answer, answerDTO.class);

        // Get info
        User user = answer.getUser();
        userDTO userDTO = userUtil.getInfoOfUser(user);
        ansDTO.setAuthor(userDTO);

        // Get votes
        voteDTO voteDTO = voteServ.getVotesOfAnswer(answer.getAnswerId());
        ansDTO.setVoteDTO(voteDTO);

        ansDTO.setIsOwner(userName.equals(answer.getUser().getUserName()));
        return ansDTO;
    }

    public Answer convertToAnswer(Discussion discussion, answerRequestDTO answerReqDTO){
        Answer answer = modelMapper.map(answerReqDTO, Answer.class);
        answer.setDiscussion(discussion);

        return answer;
    }
}
