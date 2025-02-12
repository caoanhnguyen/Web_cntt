package com.kma.converter;

import com.kma.models.sliderDTO;
import com.kma.repository.entities.Slider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class sliderDTOConverter {

    @Autowired
    ModelMapper modelMapper;

    public sliderDTO convertToSliderDTO(Slider slider){

        return modelMapper.map(slider, sliderDTO.class);
    }
}
