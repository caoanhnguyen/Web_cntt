package com.kma.services.Impl;

import com.kma.constants.fileDirection;
import com.kma.converter.articleDTOConverter;
import com.kma.models.articleDTO;
import com.kma.models.articleRequestDTO;
import com.kma.models.paginationResponseDTO;
import com.kma.repository.articleRepo;
import com.kma.repository.entities.Article;
import com.kma.repository.entities.MenuItem;
import com.kma.repository.entities.TaiNguyen;
import com.kma.repository.menuItemRepo;
import com.kma.repository.taiNguyenRepo;
import com.kma.services.articleService;
import com.kma.services.fileService;
import com.kma.utilities.taiNguyenUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@Service
@Transactional
public class articleServImpl implements articleService {

    @Autowired
    menuItemRepo menuItemRepo;
    @Autowired
    fileService fileServ;
    @Autowired
    taiNguyenRepo taiNguyenRepo;
    @Autowired
    articleRepo articleRepo;
    @Autowired
    articleDTOConverter dtoConverter;

    @Override
    public paginationResponseDTO<articleDTO> getAllArticleOfMenuItemBySlug(String slug, Integer page, Integer size) {
        // Kiểm tra slug
        if(slug.isEmpty())
            throw new IllegalArgumentException("Lỗi slug!");

        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu từ repository
        Page<Object[]> articlePage = articleRepo.findArticlesBySlug(slug, pageable);

        // Chuyển đổi Post sang postResponseDTO
        List<articleDTO> articleDTOList = articlePage.getContent().stream()
                .map(dtoConverter::convertToArticleDTO)
                .toList();

        // Đóng gói dữ liệu và meta vào DTO
        return new paginationResponseDTO<>(
                articleDTOList,
                articlePage.getTotalPages(),
                (int) articlePage.getTotalElements(),
                articlePage.isFirst(),
                articlePage.isLast(),
                articlePage.getNumber(),
                articlePage.getSize()
        );
    }

    @Override
    public void addArticle(List<MultipartFile> files, articleRequestDTO articleRequestDTO) throws IOException {
        // Kiem tra menuItem co ton tai khong
        MenuItem menuItem = menuItemRepo.findById(articleRequestDTO.getMenuItemId()).orElse(null);
        if(menuItem == null){
            throw new EntityNotFoundException("Menu not found!");
        }

        //Tạo article để lưu
        Article article = new Article();
        article.setTitle(articleRequestDTO.getTitle());
        article.setContent(articleRequestDTO.getContent());
        article.setCreateAt(new Date(System.currentTimeMillis()));
        article.setMenuItem(menuItem);
        List<TaiNguyen> tnList = article.getTaiNguyenList();

        // Upload file và lấy đường dẫn nếu cần
        if(files!=null){
            for (MultipartFile item: files) {
                // Lưu file và lấy fileCode
                String fileCode = fileServ.uploadFile(item, fileDirection.pathForTaiNguyen);
                // Tạo tài nguyên
                TaiNguyen resources = taiNguyenUtil.createResource(fileCode, article);
                // Thêm tài nguyên vào list tài nguyên của post
                tnList.add(resources);
                // Lưu tài nguyên vào DB
                taiNguyenRepo.save(resources);
            }
        }
        article.setTaiNguyenList(tnList);
        articleRepo.save(article);

    }

    @Override
    public void updateArticle(Integer articleId,
                              articleRequestDTO articleRequestDTO,
                              List<MultipartFile> files,
                              List<Integer> deleteFileIds) throws IOException {
        // Kiem tra menuItem co ton tai khong
        MenuItem menuItem = menuItemRepo.findById(articleRequestDTO.getMenuItemId()).orElse(null);
        if(menuItem == null){
            throw new EntityNotFoundException("Menu not found!");
        }

        Article article = articleRepo.findById(articleId).orElse(null);

        if(article != null) {
            //Tạo article để lưu
            article.setTitle(articleRequestDTO.getTitle());
            article.setContent(articleRequestDTO.getContent());
            article.setCreateAt(new Date(System.currentTimeMillis()));
            article.setMenuItem(menuItem);
            List<TaiNguyen> tnList = article.getTaiNguyenList();

            //Xử lí file thêm mới
            if(files != null && !files.isEmpty()) {
                for(MultipartFile file: files) {
                    // Lưu file và lấy fileCode
                    String fileCode = fileServ.uploadFile(file, fileDirection.pathForTaiNguyen);
                    // Tạo tài nguyên
                    TaiNguyen resources = taiNguyenUtil.createResource(fileCode, article);
                    // Thêm tài nguyên vào list tài nguyên của post
                    tnList.add(resources);
                    // Lưu tài nguyên vào DB
                    taiNguyenRepo.save(resources);
                }
            }

            //Xử lí các file cần xóa
            if (deleteFileIds != null) {
                for (Integer fileId : deleteFileIds) {
                    TaiNguyen tn = taiNguyenRepo.findById(fileId).orElse(null);
                    if(tn != null) {
                        // Xóa file trên server, xóa tài nguyên khỏi list tài nguyên của bài viết và xóa bản ghi tài nguyên
                        fileServ.deleteFile(tn.getResourceId(), 1);
                        tnList.remove(tn);
                        taiNguyenRepo.delete(tn);
                    }
                }
            }
            article.setTaiNguyenList(tnList);
            articleRepo.save(article);

        }else {
            throw new EntityNotFoundException("Article not found");
        }
    }

    @Override
    public void deleteArticle(Integer articleId) {
        Article existedArticle = articleRepo.findById(articleId).orElse(null);
        if(existedArticle != null) {
            List<TaiNguyen> tnlist = existedArticle.getTaiNguyenList();
            // Xóa hết các tài nguyên liên quan đến bài viết
            for(TaiNguyen tn: tnlist) {
                fileServ.deleteFile(tn.getResourceId(), 1);
            }
            articleRepo.delete(existedArticle);
        } else {
            throw new EntityNotFoundException("Article not found!");
        }
    }
}
