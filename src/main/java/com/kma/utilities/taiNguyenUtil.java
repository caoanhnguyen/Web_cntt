package com.kma.utilities;

import com.kma.repository.entities.Post;
import com.kma.repository.entities.TaiNguyen;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Date;

@Component
public class taiNguyenUtil {

    public TaiNguyen createResource(String fileCode, Post post) {
        try {
            TaiNguyen resource = new TaiNguyen();
            resource.setDescription(post.getTitle());
            resource.setCreate_at(new Date(System.currentTimeMillis()));
            resource.setFileCode(fileCode);
            resource.setPost(post);

            return resource;
        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }
    }
}
