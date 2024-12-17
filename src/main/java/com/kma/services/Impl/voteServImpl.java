package com.kma.services.Impl;

import com.kma.models.voteDTO;
import com.kma.repository.discussionRepo;
import com.kma.repository.entities.Discussion;
import com.kma.repository.voteRepo;
import com.kma.services.voteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class voteServImpl implements voteService {
    @Autowired
    discussionRepo discussRepo;
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
            dto.setUpVotes(0);
            dto.setDownVotes(0);

            if (outerResult != null && outerResult.length > 0 && outerResult[0] != null) {
                Object[] result = (Object[]) outerResult[0];

                // Kiểm tra từng phần tử trong result
                if (result[0] != null) {
                    dto.setUpVotes(Integer.parseInt(result[0].toString()));
                }
                if (result[1] != null) {
                    dto.setDownVotes(Integer.parseInt(result[1].toString()));
                }
            }

            return dto;
        } else {
            throw new EntityNotFoundException("Discussion not found!");
        }
    }

}
