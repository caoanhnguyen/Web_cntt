package com.kma.services.Impl;

import com.kma.converter.tagDTOConverter;
import com.kma.enums.TagCategory;
import com.kma.models.paginationResponseDTO;
import com.kma.models.tagDTO;
import com.kma.repository.entities.Tag;
import com.kma.repository.tagRepo;
import com.kma.services.tagService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class tagServImpl implements tagService {

    @Autowired
    tagRepo tRepo;
    @Autowired
    tagDTOConverter tagConverter;

    @Override
    public Map<TagCategory, List<tagDTO>> getGroupedTags() {
        // Lấy tất cả môn học
        List<Tag> tags = tRepo.findAllByOrderByCategory();

        // Nhóm các môn học theo danh mục
        Map<TagCategory, List<tagDTO>> groupedTags = new LinkedHashMap<>();
        for (TagCategory category : TagCategory.values()) {
            List<tagDTO> tagDTOs = tags.stream()
                    .filter(tag -> tag.getCategory() == category)
                    .map(tagConverter::convertToTagDTO)
                    .toList();
            groupedTags.put(category, tagDTOs);
        }
        return groupedTags;
    }

    @Override
    public paginationResponseDTO<tagDTO> getAllTags(Integer page, Integer size) {
        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu từ repository
        Page<Tag> tagPage = tRepo.findAll(pageable);

        // Chuyển đổi Post sang postResponseDTO
        List<tagDTO> tagDTOList = new java.util.ArrayList<>(tagPage.getContent().stream()
                .map(tagConverter::convertToTagDTO)
                .toList());

        tagDTOList.sort(Comparator.comparing(tagDTO::getTagId));

        // Đóng gói dữ liệu và meta vào DTO
        return new paginationResponseDTO<>(
                tagDTOList,
                tagPage.getTotalPages(),
                (int) tagPage.getTotalElements(),
                tagPage.isFirst(),
                tagPage.isLast(),
                tagPage.getNumber(),
                tagPage.getSize()
        );
    }
}
