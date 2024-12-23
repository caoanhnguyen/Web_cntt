package com.kma.services;

import com.kma.enums.TagCategory;
import com.kma.models.monHocResponseDTO;
import com.kma.models.paginationResponseDTO;
import com.kma.models.tagDTO;

import java.util.List;
import java.util.Map;

public interface tagService {

    paginationResponseDTO<tagDTO> getAllTags(Integer page, Integer size);

    Map<TagCategory, List<tagDTO>> getGroupedTags();
}
