package com.kma.services;

import com.kma.enums.TagCategory;
import com.kma.models.paginationResponseDTO;
import com.kma.models.tagDTO;
import com.kma.models.tagRequestDTO;

import java.util.List;
import java.util.Map;

public interface tagService {

    paginationResponseDTO<tagDTO> getAllTags(Integer page, Integer size);

    Map<TagCategory, List<tagDTO>> getGroupedTags();

    tagDTO getById(Integer tagId);

    void addTag(tagRequestDTO tagReqDTO);

    void updateTag(Integer tagId, tagRequestDTO tagReqDTO);

    void deleteTag(Integer tagId);
}
