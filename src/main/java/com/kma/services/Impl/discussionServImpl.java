package com.kma.services.Impl;

import com.kma.converter.discussionDTOConverter;
import com.kma.enums.DiscussionStatus;
import com.kma.enums.TagCategory;
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
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("discussionServ")
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
    public paginationResponseDTO<discussionResponseDTO> getAllDiscussionByTag(Integer tagId, Integer page, Integer size, String sort, String order) {
        // Lấy sortCriterial
        Sort sortCriteria = handleSort(sort,order);

        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size, sortCriteria);

        // Lấy dữ liệu từ repository
        Page<Object[]> discussPage = discussRepo.findByTag(tagId, pageable);

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
    public discussionDTO getById(Integer discussionId) {

        Discussion discussion = discussRepo.findById(discussionId).orElse(null);
        if(discussion!=null) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return discussDTOConverter.convertToDiscussDTO(discussion, user.getUserName());
        }else {
            throw new EntityNotFoundException("Discussion not found!");
        }
    }

    @Override
    public paginationResponseDTO<discussionResponseDTO> getAllDiscussionOfUser(Map<String,Object> params, Integer page, Integer size, String sort, String order) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Lấy sortCriterial
        Sort sortCriteria = handleSort(sort,order);

        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size, sortCriteria);

        // Lấy dữ liệu từ repository
        Page<Object[]> discussPage = fetchDiscussionOfUser(params, user.getUserId(), pageable);

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

    private Page<Object[]> fetchDiscussionOfUser(Map<String, Object> params, Integer userId, Pageable pageable){
        // Lấy giá trị từ params
        String title = ( params.get("title") != null ? (String) params.get("title") : "");
        String tagName = ( params.get("tagName") != null ? (String) params.get("tagName") : "");
        String category = ( params.get("tag_category") != null ? (String) params.get("tag_category") : "");

        if(!category.isEmpty()){
            try {
                category = TagCategory.valueOf(category).toString();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid tag category value: " + category);
            }
        }

        // Lấy dữ liệu từ repository
        return discussRepo.findByUser_UserId(userId, title, tagName, category, pageable);
    }

    @Override
    public paginationResponseDTO<discussionResponseDTO> getAllDiscussion(Map<String, Object> params, Integer page, Integer size, String sort, String order) {

        // Lấy sortCriterial
        Sort sortCriteria = handleSort(sort,order);

        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size, sortCriteria);

        // Lấy dữ liệu từ repository
        Page<Object[]> discussPage = fetchDiscussion(params, pageable);

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

    private Sort handleSort(String sort, String order) {
        // Xử lý Sort theo tiêu chí và thứ tự
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Xác định trường sắp xếp dựa trên sort
        return switch (sort) {
            case "score" ->  // Sắp xếp theo điểm
                    Sort.by(direction, "score");
            case "answers" ->  // Sắp xếp theo số câu trả lời
                    Sort.by(direction, "answer_count");  // Sắp xếp theo ngày tạo
            default -> Sort.by(direction, "createAt");
        };
    }

    private Page<Object[]> fetchDiscussion(Map<String, Object> params, Pageable pageable){
        // Lấy giá trị từ params
        String title = ( params.get("title") != null ? (String) params.get("title") : "");
        String tagName = ( params.get("tagName") != null ? (String) params.get("tagName") : "");
        String category = ( params.get("tag_category") != null ? (String) params.get("tag_category") : "");

        if(!category.isEmpty()){
            try {
                category = TagCategory.valueOf(category).toString();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid tag category value: " + category);
            }
        }

        // Lấy dữ liệu từ repository
        return discussRepo.findByAllCondition(title, tagName, category, pageable);
    }

    @Override
    public paginationResponseDTO<discussionResponseDTO> getAllPendingDiscuss(Integer page, Integer size) {
        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu từ repository
        Page<Object[]> discussPage = discussRepo.findByStatus(pageable);

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
    public void addDiscussion(discussionRequestDTO discussReqDTO, List<Integer> tagIdList) {
        // Lấy thông tin người đăng
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Discussion discussion = new Discussion();
        discussDTOConverter.convertToDiscussion(discussReqDTO, discussion, tagIdList);
        discussion.setUser(user);

        discussion.setStatus(DiscussionStatus.PENDING);

        discussRepo.save(discussion);
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
    public void updateDiscussionStatus(List<Integer> discussionIds, String discussionStatus) {
        for(Integer discussionId: discussionIds) {

            // Kiểm tra xem discussion có tồn tại không
            Discussion discussion = discussRepo.findById(discussionId).orElse(null);
            if (discussion != null) {
                // Chuyển đổi discussionStatus từ String sang Enum
                DiscussionStatus status;
                try {
                    status = DiscussionStatus.valueOf(discussionStatus.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid discussion status: " + discussionStatus);
                }

                User user = discussion.getUser();
                String userId = user.getUserName();

                if (status.equals(DiscussionStatus.REJECTED)) {
                    // Xóa bài thảo luận khỏi DB
                    discussRepo.delete(discussion);

                    // Thông báo đến user đó là discussion bị reject
                    String title = "Bài thảo luận bị từ chối!";
                    String content = "Bài thảo luận với tiêu đề: " + discussion.getTitle() + " của bạn đã bị từ chối!";
                    notiServ.sendNotificationByUserId(userId, title, content, "");
                } else if (status.equals(DiscussionStatus.APPROVED)) {
                    // Duyệt bài thảo luận
                    discussion.setStatus(DiscussionStatus.APPROVED);
                    discussRepo.save(discussion);

                    // Thông báo đến user đó là discussion đã được duyệt
                    String title = "Bài thảo luận của bạn đã được duyệt!";
                    String content = "Bài thảo luận với tiêu đề: " + discussion.getTitle() + " của bạn đã được duyệt!";
                    String url = "/api/discussions/"+discussionId;
                    notiServ.sendNotificationByUserId(userId, title, content, url);
                }

            } else {
                throw new EntityNotFoundException("Discussion not found!");
            }
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

    @Override
    public boolean isOwner(Integer discussionId, Integer userId) {
        boolean isOwner = discussRepo.existsByDiscussionIdAndUser_UserId(discussionId, userId);
        if (!isOwner) {
            throw new AccessDeniedException("You do not have permission to modify this discussion.");
        }
        return true;
    }

}
