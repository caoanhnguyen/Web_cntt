package com.kma.repository;

import java.util.List;
import com.kma.repository.custom.postRepoCustom;
import com.kma.repository.entities.NhanVien;
import com.kma.repository.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface postRepo extends JpaRepository<Post, Integer>, postRepoCustom {

    List<Post> findByTitleContainingAndNhanVien_idUser(String title, Integer idUser);

    List<Post> findByTitleContainingOrderByPostIdDesc(String title);

    List<Post> findByNhanVien_idUserOrderByPostIdDesc(Integer idUser);

    List<Post> findAllByOrderByPostIdDesc();

    List<Post> findTop6ByOrderByPostIdDesc();
}
