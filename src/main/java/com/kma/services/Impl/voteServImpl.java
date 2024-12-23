package com.kma.services.Impl;

import com.kma.enums.VoteType;
import com.kma.models.voteDTO;
import com.kma.repository.answerRepo;
import com.kma.repository.discussionRepo;
import com.kma.repository.entities.Answer;
import com.kma.repository.entities.Discussion;
import com.kma.repository.entities.User;
import com.kma.repository.entities.Vote;
import com.kma.repository.voteRepo;
import com.kma.services.voteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Transactional
public class voteServImpl implements voteService {
    @Autowired
    discussionRepo discussRepo;
    @Autowired
    answerRepo ansRepo;
    @Autowired
    voteRepo voteRepo;

    @Override
    public voteDTO getVotesOfDiscussion(Integer discussionId) {
        // Kiểm tra discussion có tồn tại không
        Discussion discussion = discussRepo.findById(discussionId).orElse(null);
        if (discussion != null) {
            Object[] outerResult = voteRepo.getVotesOfDiscussion(discussionId);

            // Khởi tạo DTO với giá trị mặc định là 0
            voteDTO dto = new voteDTO();
            dto.setUpVotes(0L);
            dto.setDownVotes(0L);

            if (outerResult != null && outerResult.length > 0 && outerResult[0] != null) {
                Object[] result = (Object[]) outerResult[0];

                // Kiểm tra từng phần tử trong result
                if (result[0] != null) {
                    dto.setUpVotes((long) Integer.parseInt(result[0].toString()));
                }
                if (result[1] != null) {
                    dto.setDownVotes((long) Integer.parseInt(result[1].toString()));
                }
            }

            // Check isVoted
            isVoted(dto, discussionId, null);

            return dto;
        } else {
            throw new EntityNotFoundException("Discussion not found!");
        }
    }

    @Override
    public voteDTO getVotesOfAnswer(Integer answerId) {
        // Kiểm tra answer có tồn tại không
        Answer answer = ansRepo.findById(answerId).orElse(null);
        if (answer != null) {
            Object[] outerResult = voteRepo.getVotesOfAnswer(answerId);

            // Khởi tạo DTO với giá trị mặc định là 0
            voteDTO dto = new voteDTO();
            dto.setUpVotes(0L);
            dto.setDownVotes(0L);

            if (outerResult != null && outerResult.length > 0 && outerResult[0] != null) {
                Object[] result = (Object[]) outerResult[0];

                // Kiểm tra từng phần tử trong result
                if (result[0] != null) {
                    dto.setUpVotes((long) Integer.parseInt(result[0].toString()));
                }
                if (result[1] != null) {
                    dto.setDownVotes((long) Integer.parseInt(result[1].toString()));
                }
            }

            // Check isVoted
            isVoted(dto, null, answerId);

            return dto;
        } else {
            throw new EntityNotFoundException("Answer not found!");
        }
    }

    private void isVoted(voteDTO dto, Integer discussionId, Integer answerId){

        // Kiểm tra xem user trong principal đã vote chưa, nếu rồi thì là loại gì
        User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Vote> existingVote = Optional.empty();
        if(discussionId!=null){
            existingVote = voteRepo.findByUser_UserIdAndDiscussion_DiscussionId(user.getUserId(), discussionId);
        }else if(answerId!=null){
            existingVote = voteRepo.findByUser_UserIdAndAnswer_AnswerId(user.getUserId(), answerId);
        }

        if(existingVote.isEmpty()){
            dto.setVote(VoteType.NONE);
        }else{
            dto.setVote(existingVote.get().getType());
        }
    }

    @Override
    public void voteDiscussion(Integer discussionId, String voteType) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Kiểm tra xem discussion có tồn tại không
        Discussion discussion = discussRepo.findById(discussionId)
                .orElseThrow(() -> new EntityNotFoundException("Discussion not found!"));

        // Kiểm tra vote có tồn tại hay chưa
        Optional<Vote> existingVote = voteRepo.findByUser_UserIdAndDiscussion_DiscussionId(user.getUserId(), discussionId);

        // Chuyển đổi voteType từ String sang Enum
        VoteType newVoteType;
        try {
            newVoteType = VoteType.valueOf(voteType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid vote type");
        }

        if (existingVote.isPresent()) {
            // Nếu vote đã tồn tại
            Vote vote = existingVote.get();
            if (!vote.getType().equals(newVoteType)) {
                // Nếu voteType khác thì update
                vote.setType(newVoteType);
                voteRepo.save(vote);
            }
            // Nếu giống thì không làm gì
        } else {
            // Nếu vote chưa tồn tại, tạo mới
            Vote newVote = new Vote();
            newVote.setUser(user); // Tạo user hoặc lấy user đã tồn tại
            newVote.setDiscussion(discussion);
            newVote.setType(newVoteType);
            voteRepo.save(newVote);
        }
    }


    @Override
    public void voteAnswer(Integer answerId, String voteType) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Kiểm tra xem answer có tồn tại không
        Answer answer = ansRepo.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("Answer not found!"));

        // Kiểm tra vote có tồn tại hay chưa
        Optional<Vote> existingVote = voteRepo.findByUser_UserIdAndAnswer_AnswerId(user.getUserId(), answerId);

        // Chuyển đổi voteType từ String sang Enum
        VoteType newVoteType;
        try {
            newVoteType = VoteType.valueOf(voteType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid vote type");
        }

        if (existingVote.isPresent()) {
            // Nếu vote đã tồn tại
            Vote vote = existingVote.get();
            if (!vote.getType().equals(newVoteType)) {
                // Nếu voteType khác thì update
                vote.setType(newVoteType);
                voteRepo.save(vote);
            }
            // Nếu giống thì không làm gì
        } else {
            // Nếu vote chưa tồn tại, tạo mới
            Vote newVote = new Vote();
            newVote.setUser(user); // Tạo user hoặc lấy user đã tồn tại
            newVote.setAnswer(answer);
            newVote.setType(newVoteType);
            voteRepo.save(newVote);
        }
    }

    @Override
    public boolean isVoted(Integer discussionId, Integer answerId) {
        // Lấy userId từ Principal
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Kiểm tra vote tùy vào discussionId hoặc answerId
        if (discussionId != null) {
            return voteRepo.existsByUser_UserIdAndDiscussion_DiscussionId(user.getUserId(), discussionId);
        } else if (answerId != null) {
            return voteRepo.existsByUser_UserIdAndAnswer_AnswerId(user.getUserId(), answerId);
        }

        throw new IllegalArgumentException("Both discussionId and answerId cannot be null");
    }

}
