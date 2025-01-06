package com.kma.services;

import com.kma.models.articleDTO;
import com.kma.models.articleRequestDTO;
import com.kma.models.paginationResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface articleService {

    paginationResponseDTO<articleDTO> getAllArticleOfMenuItemBySlug(String slug, Integer page, Integer size);

    void addArticle(List<MultipartFile> files, articleRequestDTO articleRequestDTO) throws IOException;

    void updateArticle(Integer articleId,
                       articleRequestDTO articleRequestDTO,
                       List<MultipartFile> files,
                       List<Integer> deleteFileIds) throws IOException;

    void deleteArticle(Integer articleId);
}
