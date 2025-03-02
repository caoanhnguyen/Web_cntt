package com.kma.repository;

import java.util.List;
import com.kma.repository.custom.postRepoCustom;
import com.kma.repository.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface postRepo extends JpaRepository<Post, Integer>, postRepoCustom {

    //JPQL
    @Query("SELECT p FROM Post p LEFT JOIN p.nhanVien nv " +
            "WHERE p.title LIKE %:title% " +
            "AND (nv.tenNhanVien LIKE %:authorName% OR nv IS NULL) " +
            "ORDER BY p.createAt DESC" +
            ", p.postId DESC")
    Page<Post> findByAllCondition(@Param("title") String title,@Param("authorName") String authorName, Pageable pageable);

    List<Post> findTop6ByOrderByCreateAtDesc();

    Page<Post> findByNhanVien_IdUserOrderByCreateAtDesc(Integer authorId, Pageable pageable);

    boolean existsByPostIdAndNhanVien_IdUser(Integer postId, Integer authorId);
}
