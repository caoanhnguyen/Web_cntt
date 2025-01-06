package com.kma.converter;

import com.kma.models.*;
import com.kma.repository.articleRepo;
import com.kma.repository.entities.Article;
import com.kma.repository.entities.TaiNguyen;
import com.kma.services.fileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
public class articleDTOConverter {

    @Autowired
    fileService fileServ;
    @Autowired
    articleRepo artiRepo;

    public articleDTO convertToArticleDTO(Object[] row){
        // Tạo đối tượng DTO mới
        articleDTO dto = new articleDTO();
        menuItemDTO menuDTO = new menuItemDTO();
        List<fileDTO> fileDTO = new ArrayList<>();

        // Gán các giá trị từ Object[] vào DTO
        dto.setArticleId((Integer) row[0]); // discussionId
        dto.setTitle((String) row[1]); // title
        dto.setContent((String) row[2]); // content
        dto.setCreateAt((Date) row[3]); // createAt

        // Gán thông tin menu item
        menuDTO.setId((Integer) row[4]); // id
        menuDTO.setTitle((String) row[5]); // title
        menuDTO.setSlug((String) row[6]); // slug
        dto.setMenuDTO(menuDTO);

        // File DTO
        Article article = artiRepo.findById(dto.getArticleId()).orElse(null);
        if(article != null){
            List<TaiNguyen> tnList = article.getTaiNguyenList();
            fileDTO = fileServ.getListFileDTO(tnList);
        }
        dto.setFile_dto(fileDTO);

        return dto;
    }
}
