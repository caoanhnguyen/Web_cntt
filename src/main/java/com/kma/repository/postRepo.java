package com.kma.repository;

import java.util.List;

import com.kma.models.postRequestDTO;
import com.kma.repository.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface postRepo extends JpaRepository<Post, Integer> {
    List<Post> findByTitleContainingAndNhanVien_idUser(String title, Integer idUser);

    List<Post> findByTitleContaining(String title);

    List<Post> findByNhanVien_idUser(Integer idUser);
}
