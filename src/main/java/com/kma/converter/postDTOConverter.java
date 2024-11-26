package com.kma.converter;

import com.kma.models.fileDTO;
import com.kma.models.postDTO;
import com.kma.models.postResponseDTO;
import com.kma.repository.entities.Post;
import com.kma.repository.entities.TaiNguyen;
import com.kma.services.fileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class postDTOConverter {

    @Autowired
    fileService fileServ;
    @Autowired
    ModelMapper modelMapper;

    public postDTO convertToPostDTO(Post items){
        postDTO dto = modelMapper.map(items, postDTO.class);
        dto.setCreate_at(items.getCreateAt());
        List<TaiNguyen> tnList = items.getTaiNguyenList();
        List<fileDTO> fileDTO = fileServ.getListFileDTO(tnList);

        dto.setFile_dto(fileDTO);
        dto.setAuthor(items.getNhanVien().getTenNhanVien());

        return dto;
    }

    public postResponseDTO convertToPostResDTO(Post item){
        postResponseDTO postResDTO = modelMapper.map(item, postResponseDTO.class);
        String authorName = item.getNhanVien().getTenNhanVien();

        postResDTO.setAuthorName(authorName);
        return postResDTO;
    }
}
