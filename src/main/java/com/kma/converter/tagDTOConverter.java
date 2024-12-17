package com.kma.converter;

import com.kma.models.tagDTO;
import com.kma.repository.entities.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class tagDTOConverter {
    @Autowired
    ModelMapper modelMapper;

    public tagDTO convertToTagDTO(Tag tag){
        return modelMapper.map(tag, tagDTO.class);
    }
}
