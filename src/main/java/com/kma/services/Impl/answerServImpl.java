package com.kma.services.Impl;

import com.kma.converter.answerDTOConverter;
import com.kma.models.answerDTO;
import com.kma.models.answerRequestDTO;
import com.kma.models.paginationResponseDTO;
import com.kma.models.userDTO;
import com.kma.repository.answerRepo;
import com.kma.repository.discussionRepo;
import com.kma.repository.entities.Answer;
import com.kma.repository.entities.Discussion;
import com.kma.repository.entities.User;
import com.kma.services.answerService;
import com.kma.utilities.userInfoUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service("answerServ")
@Transactional
public class answerServImpl implements answerService {
    @Autowired
    answerRepo ansRepo;
    @Autowired
    discussionRepo discussRepo;
    @Autowired
    answerDTOConverter ansDTOConverter;
    @Autowired
    NotificationService notiServ;
    @Autowired
    userInfoUtil infoUtil;

    @Override
    public paginationResponseDTO<answerDTO> getAllAnswerOfDiscussion(Integer discussionId, Integer page, Integer size) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu từ repository
        Page<Answer> answerPage = ansRepo.findByDiscussion_DiscussionIdOrderByCreateAtDesc(discussionId, pageable);

        // Chuyển đổi Post sang postResponseDTO
        List<answerDTO> answerDTOList = answerPage.getContent().stream()
                .map(i->ansDTOConverter.convertToAnswerDTO(i, user.getUserName()))
                .toList();

        // Đóng gói dữ liệu và meta vào DTO
        return new paginationResponseDTO<>(
                answerDTOList,
                answerPage.getTotalPages(),
                (int) answerPage.getTotalElements(),
                answerPage.isFirst(),
                answerPage.isLast(),
                answerPage.getNumber(),
                answerPage.getSize()
        );
    }

    @Override
    public void addAnswer(Integer discussionId, answerRequestDTO ansReqDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Kiểm tra xem discussion có tồn tại không
        Discussion discussion = discussRepo.findById(discussionId)
                .orElseThrow(() -> new EntityNotFoundException("Discussion not found!"));

        // Tạo answer
        Answer answer = new Answer();
        ansDTOConverter.convertToAnswer(discussion, answer, ansReqDTO);
        answer.setUser(user);
        ansRepo.save(answer);

        // Gửi thông báo đến chủ bài thảo luận
        User author = discussion.getUser();
        userDTO answer_author = infoUtil.getInfoOfUser(user);

        String userId = author.getUserName();
        String title = "Reply discussion!";
        String body =  answer_author.getName() + " đã trả lời bài thảo luận của bạn!";
        String url = "api/discussions/"+ discussionId;
        notiServ.sendNotificationByUserId(userId, title, body, url);
    }

    @Override
    public void updateAnswer(Integer discussionId, Integer answerId, answerRequestDTO ansReqDTO) {
        Answer answer = ansRepo.findByAnswerIdAndDiscussion_DiscussionId(answerId, discussionId)
                .orElseThrow(() -> new EntityNotFoundException("Answer not found or does not belong to the specified discussion"));

        answer.setContent(ansReqDTO.getContent());
        answer.setCreateAt(new Date(System.currentTimeMillis()));

        ansRepo.save(answer);
    }


    @Override
    public void deleteAnswer(Integer discussionId, Integer answerId) {
        Answer answer = ansRepo.findByAnswerIdAndDiscussion_DiscussionId(answerId, discussionId)
                .orElseThrow(() -> new EntityNotFoundException("Answer not found or does not belong to the specified discussion"));

        ansRepo.delete(answer);
    }

    @Override
    public boolean isOwner(Integer answerId, Integer userId) {
        boolean isOwner = ansRepo.existsByAnswerIdAndUser_UserId(answerId, userId);
        if (!isOwner) {
            throw new AccessDeniedException("You do not have permission to modify this discussion.");
        }
        return true;
    }
}
