package com.kma.utilities;

import com.kma.repository.entities.Article;
import com.kma.repository.entities.Post;
import com.kma.repository.entities.SuKien;
import com.kma.repository.entities.TaiNguyen;
import java.sql.Date;

public class taiNguyenUtil {

    public static TaiNguyen createResource(String fileCode, Object object) {
        try {
            TaiNguyen resource = new TaiNguyen();
            if(object instanceof Post){
                resource.setDescription(((Post) object).getTitle());
                resource.setCreateAt(new Date(System.currentTimeMillis()));
                resource.setFileCode(fileCode);
                resource.setPost((Post) object);
            }
            if(object instanceof SuKien){
                resource.setDescription(((SuKien) object).getDescription());
                resource.setCreateAt(new Date(System.currentTimeMillis()));
                resource.setFileCode(fileCode);
                resource.setEvent((SuKien) object);
            }
            if(object instanceof Article){
                resource.setDescription(((Article) object).getTitle());
                resource.setCreateAt(new Date(System.currentTimeMillis()));
                resource.setFileCode(fileCode);
                resource.setArticle((Article) object);
            }

            return resource;
        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }
    }
}
