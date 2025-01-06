package com.kma.repository;

import com.kma.repository.entities.Article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface articleRepo extends JpaRepository<Article, Integer> {
    @Query(value = "SELECT " +
            "a.articleId AS articleId, " +
            "a.title AS title, " +
            "a.content AS content, " +
            "a.createAt AS createAt, " +
            "mn.id AS menuItemId, " +
            "mn.title AS menuTitle, " +
            "mn.slug AS slug " +
            "FROM article a " +
            "LEFT JOIN menu_item mn ON a.menuItemId = mn.id " +
            "WHERE mn.slug = :slug " +
            "ORDER BY a.createAt DESC, a.articleId DESC",
            nativeQuery = true)
    Page<Object[]> findArticlesBySlug(@Param("slug") String slug, Pageable pageable);
}
