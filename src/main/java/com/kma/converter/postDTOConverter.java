package com.kma.converter;

import com.kma.models.fileDTO;
import com.kma.models.postDTO;
import com.kma.repository.entities.Post;
import com.kma.repository.entities.TaiNguyen;
import com.kma.services.fileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class postDTOConverter {

    @Autowired
    fileService fileServ;

    public postDTO convertToPostDTO(Post items){
        postDTO dto = new postDTO();
        dto.setPost_id(items.getPost_id());
        dto.setTitle(items.getTitle());
        dto.setContent(items.getContent());
        dto.setCreate_at(items.getCreateAt());

        List<TaiNguyen> tnList = items.getTaiNguyenList();
        List<fileDTO> fileDTO = fileServ.getListFileDTO(tnList);

        dto.setFile_dto(fileDTO);
        dto.setAuthor(items.getNhanVien().getTenNhanVien());

        return dto;
    }
}
