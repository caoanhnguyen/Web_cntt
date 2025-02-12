package com.kma.services;

import com.kma.models.paginationResponseDTO;
import com.kma.models.sliderDTO;

import java.util.List;

public interface sliderService {
    paginationResponseDTO<sliderDTO> getAllSlider(Integer page, Integer size, String sort, String order);

    List<sliderDTO> getLatestSlider();

    sliderDTO getById(Integer sliderId);

    void addSlider(sliderDTO DTO);

    void updateSlider(Integer sliderId, sliderDTO DTO);

    void deleteSlider(Integer sliderId);
}
