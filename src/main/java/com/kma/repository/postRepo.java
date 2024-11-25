package com.kma.repository;

import java.util.List;
import com.kma.repository.custom.postRepoCustom;
import com.kma.repository.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface postRepo extends JpaRepository<Post, Integer>, postRepoCustom {

    Page<Post> findByTitleContainingAndNhanVien_idUserInOrderByPostIdDesc(String title, List<Integer> idUsers, Pageable pageable);

    Page<Post> findByTitleContainingOrderByPostIdDesc(String title, Pageable pageable);

    Page<Post> findByNhanVien_idUserInOrderByPostIdDesc(List<Integer> idUsers, Pageable pageable);

    Page<Post> findAllByOrderByPostIdDesc(Pageable pageable);

    List<Post> findTop6ByOrderByPostIdDesc();
}
