package com.kma.services.Impl;

import com.kma.converter.discussionDTOConverter;
import com.kma.enums.DiscussionStatus;
import com.kma.models.discussionDTO;
import com.kma.models.discussionRequestDTO;
import com.kma.models.discussionResponseDTO;
import com.kma.models.paginationResponseDTO;
import com.kma.repository.discussionRepo;
import com.kma.repository.entities.Discussion;
import com.kma.repository.entities.Tag;
import com.kma.repository.entities.User;
import com.kma.repository.tagRepo;
import com.kma.services.discussionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class discussionServImpl implements discussionService {

    @Autowired
    discussionRepo discussRepo;
    @Autowired
    discussionDTOConverter discussDTOConverter;
    @Autowired
    tagRepo tRepo;
    @Autowired
    NotificationService notiServ;

    @Override
    public discussionDTO getById(Integer discussionId) {
        Discussion discussion = discussRepo.findById(discussionId).orElse(null);
        if(discussion!=null)
            return discussDTOConverter.convertToDiscussDTO(discussion);
        throw new EntityNotFoundException("Discussion not found!");
    }

    @Override
    public paginationResponseDTO<discussionResponseDTO> getAllDiscussion(Map<String, Object> params, Integer page, Integer size) {
        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu từ repository
        Page<Discussion> discussPage = fetchDiscussion(params, pageable);

        // Chuyển đổi Post sang postResponseDTO
        List<discussionResponseDTO> discussResDTOList = discussPage.getContent().stream()
                .map(discussDTOConverter::convertToDiscussResDTO)
                .toList();

        // Đóng gói dữ liệu và meta vào DTO
        return new paginationResponseDTO<>(
                discussResDTOList,
                discussPage.getTotalPages(),
                (int) discussPage.getTotalElements(),
                discussPage.isFirst(),
                discussPage.isLast(),
                discussPage.getNumber(),
                discussPage.getSize()
        );
    }

    private Page<Discussion> fetchDiscussion(Map<String, Object> params, Pageable pageable){
        // Lấy giá trị từ params
        String title = ( params.get("title") != null ? (String) params.get("title") : "");
        String tagName = ( params.get("tagName") != null ? (String) params.get("tagName") : "");

        // Lấy dữ liệu từ repository
        return discussRepo.findByAllCondition(title, tagName, pageable);
    }

    @Override
    public paginationResponseDTO<discussionResponseDTO> getAllPendingDiscuss(Integer page, Integer size) {
        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu từ repository
        Page<Discussion> discussPage = discussRepo.findByStatus(DiscussionStatus.PENDING, pageable);

        // Chuyển đổi Post sang postResponseDTO
        List<discussionResponseDTO> discussResDTOList = discussPage.getContent().stream()
                .map(discussDTOConverter::convertToDiscussResDTO)
                .toList();

        // Đóng gói dữ liệu và meta vào DTO
        return new paginationResponseDTO<>(
                discussResDTOList,
                discussPage.getTotalPages(),
                (int) discussPage.getTotalElements(),
                discussPage.isFirst(),
                discussPage.isLast(),
                discussPage.getNumber(),
                discussPage.getSize()
        );
    }

    @Override
    public List<discussionResponseDTO> getLatestDiscussions() {
        // Tìm kiếm bài viết mới nhất
        List<Discussion> posts = discussRepo.findTop6ByStatusOrderByDiscussionIdDesc(DiscussionStatus.APPROVED);
        return posts.stream()
                .map(discussDTOConverter::convertToDiscussResDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void addDiscussion(discussionRequestDTO discussReqDTO, List<Integer> tagIdList, Principal principal) {
        // Lấy thông tin người đăng
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Discussion discussion = discussDTOConverter.convertToDiscussion(discussReqDTO, tagIdList);
        discussion.setUser(user);

        discussion.setStatus(DiscussionStatus.PENDING);

        discussRepo.save(discussion);

        // Gửi thông báo bài viết mới
        String title = "Có bài thảo luận mới. Xem ngay!";
        String content = discussReqDTO.getTitle();
        notiServ.sendNotificationToAllUsers(title, content);
    }

    @Override
    public void updateDiscussion(Integer discussionId, discussionRequestDTO discussReqDTO, List<Integer> tagIdList) {
        // Kiểm tra xem discussion có tồn tại không
        Discussion discussion = discussRepo.findById(discussionId).orElse(null);
        if(discussion!=null){
            // Nếu tồn tại thì update
            discussion.setTitle(discussReqDTO.getTitle());
            discussion.setContent(discussReqDTO.getContent());
            discussion.setCreateAt(new Date(System.currentTimeMillis()));

            // Xóa toàn bộ tags hiện tại và gán tags mới
            Set<Tag> newTags = new HashSet<>();
            for (Integer tagId : tagIdList) {
                Tag tag = tRepo.findById(tagId).orElseThrow(() ->
                        new EntityNotFoundException("Tag not found with ID: " + tagId));
                newTags.add(tag);
            }
            discussion.setTags(newTags);

            // Lưu lại
            discussRepo.save(discussion);
        }else{
            throw new EntityNotFoundException("Discussion not found!");
        }

    }

    @Override
    public void updateDiscussionStatus(Integer discussionId, String discussionStatus) {
        // Kiểm tra xem discussion có tồn tại không
        Discussion discussion = discussRepo.findById(discussionId).orElse(null);
        if(discussion!=null){
            // Chuyển đổi discussionStatus từ String sang Enum
            DiscussionStatus status;
            try {
                status = DiscussionStatus.valueOf(discussionStatus.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid discussion status: " + discussionStatus);
            }

            if(status.equals(DiscussionStatus.REJECTED)){
                // Xóa bài thảo luận khỏi DB
                discussRepo.delete(discussion);

                // Thông báo đến user đó là discussion bị reject
                User user = discussion.getUser();
                String userId = user.getUserName();
                String title = "Bài thảo luận bị từ chối!";
                String content = "Bài thảo luận với tiêu đề: " + discussion.getTitle() + " của bạn đã bị từ chối!";
                notiServ.sendNotificationByUserId(userId, title, content);
            }else if(status.equals(DiscussionStatus.APPROVED)){
                // Duyệt bài thảo luận
                discussion.setStatus(DiscussionStatus.APPROVED);
            }

        }else{
            throw new EntityNotFoundException("Discussion not found!");
        }
    }

    @Override
    public void deleteDiscussion(Integer discussionId) {
        // Kiểm tra xem discussion có tồn tại không
        Discussion discussion = discussRepo.findById(discussionId).orElse(null);
        if(discussion!=null){
            discussRepo.delete(discussion);
        }else{
            throw new EntityNotFoundException("Discussion not found!");
        }
    }
}
