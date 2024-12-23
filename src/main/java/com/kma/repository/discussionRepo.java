package com.kma.repository;

import com.kma.repository.entities.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface discussionRepo extends JpaRepository<Discussion, Integer> {

    @Query(value = """
    SELECT * FROM (
            SELECT DISTINCT
                    dis.discussionId,\s
                    dis.title,
                    dis.content,\s
                    dis.createAt,\s
                    dis.status,
                   \s
                    -- Lấy thông tin người đăng bài
                    CASE\s
                        WHEN u.userType = 'SINHVIEN' THEN sv.maSinhVien
                        WHEN u.userType = 'NHANVIEN' THEN nv.idUser
                        ELSE NULL
                    END AS author_id,
                    CASE\s
                        WHEN u.userType = 'SINHVIEN' THEN sv.tenSinhVien
                        WHEN u.userType = 'NHANVIEN' THEN nv.tenNhanVien
                        ELSE NULL
                    END AS author_name,
                    CASE\s
                        WHEN u.userType = 'SINHVIEN' THEN sv.avaFileCode
                        WHEN u.userType = 'NHANVIEN' THEN nv.avaFileCode
                        ELSE NULL
                    END AS author_avatar,
                   \s
                    -- Tính điểm tổng
                    (SELECT COALESCE(SUM(CASE\s
                        WHEN v.type = 'UP' THEN 1\s
                        WHEN v.type = 'DOWN' THEN -1\s
                        ELSE 0 END), 0)\s
                     FROM vote v\s
                     WHERE v.discussionId = dis.discussionId) AS score,\s
                   \s
                    -- Số lượng upvote
                    (SELECT COUNT(*)\s
                     FROM vote v\s
                     WHERE v.discussionId = dis.discussionId AND v.type = 'UP') AS upvote_count,
    
                    -- Số lượng downvote
                    (SELECT COUNT(*)\s
                     FROM vote v\s
                     WHERE v.discussionId = dis.discussionId AND v.type = 'DOWN') AS downvote_count,
    
                    -- Số lượng câu trả lời
                    (SELECT COUNT(*)\s
                     FROM answer a\s
                     WHERE a.discussionId = dis.discussionId) AS answer_count
    
            FROM discussion dis
            LEFT JOIN discussion_tag dt ON dt.discussionId = dis.discussionId
            LEFT JOIN tag t ON dt.tagId = t.tagId
    
            -- Join với bảng user
            LEFT JOIN user u ON u.userId = dis.userId
    
            -- Join thêm với bảng sinhvien và nhanvien để lấy thông tin chi tiết người dùng
            LEFT JOIN sinh_vien sv ON u.userId = sv.userId
            LEFT JOIN nhan_vien nv ON u.userId = nv.userId

    WHERE (t.tagId = :tagId)
    ) AS temp
    """,
            nativeQuery = true)
    Page<Object[]> findByTag(@Param("tagId") Integer tagId, Pageable pageable);

    @Query(value = """
    SELECT * FROM (
            SELECT DISTINCT
                    dis.discussionId,\s
                    dis.title,
                    dis.content,\s
                    dis.createAt,\s
                    dis.status,
                   \s
                    -- Lấy thông tin người đăng bài
                    CASE\s
                        WHEN u.userType = 'SINHVIEN' THEN sv.maSinhVien
                        WHEN u.userType = 'NHANVIEN' THEN nv.idUser
                        ELSE NULL
                    END AS author_id,
                    CASE\s
                        WHEN u.userType = 'SINHVIEN' THEN sv.tenSinhVien
                        WHEN u.userType = 'NHANVIEN' THEN nv.tenNhanVien
                        ELSE NULL
                    END AS author_name,
                    CASE\s
                        WHEN u.userType = 'SINHVIEN' THEN sv.avaFileCode
                        WHEN u.userType = 'NHANVIEN' THEN nv.avaFileCode
                        ELSE NULL
                    END AS author_avatar,
                   \s
                    -- Tính điểm tổng
                    (SELECT COALESCE(SUM(CASE\s
                        WHEN v.type = 'UP' THEN 1\s
                        WHEN v.type = 'DOWN' THEN -1\s
                        ELSE 0 END), 0)\s
                     FROM vote v\s
                     WHERE v.discussionId = dis.discussionId) AS score,\s
                   \s
                    -- Số lượng upvote
                    (SELECT COUNT(*)\s
                     FROM vote v\s
                     WHERE v.discussionId = dis.discussionId AND v.type = 'UP') AS upvote_count,
    
                    -- Số lượng downvote
                    (SELECT COUNT(*)\s
                     FROM vote v\s
                     WHERE v.discussionId = dis.discussionId AND v.type = 'DOWN') AS downvote_count,
    
                    -- Số lượng câu trả lời
                    (SELECT COUNT(*)\s
                     FROM answer a\s
                     WHERE a.discussionId = dis.discussionId) AS answer_count
    
            FROM discussion dis
            LEFT JOIN discussion_tag dt ON dt.discussionId = dis.discussionId
            LEFT JOIN tag t ON dt.tagId = t.tagId
    
            -- Join với bảng user
            LEFT JOIN user u ON u.userId = dis.userId
    
            -- Join thêm với bảng sinhvien và nhanvien để lấy thông tin chi tiết người dùng
            LEFT JOIN sinh_vien sv ON u.userId = sv.userId
            LEFT JOIN nhan_vien nv ON u.userId = nv.userId

    WHERE (dis.status = 'PENDING')
    ) AS temp
    """,
            nativeQuery = true)
    Page<Object[]> findByStatus(Pageable pageable);

    @Query(value = """
    SELECT * FROM (
            SELECT DISTINCT
                    dis.discussionId,\s
                    dis.title,
                    dis.content,\s
                    dis.createAt,\s
                    dis.status,
                   \s
                    -- Lấy thông tin người đăng bài
                    CASE\s
                        WHEN u.userType = 'SINHVIEN' THEN sv.maSinhVien
                        WHEN u.userType = 'NHANVIEN' THEN nv.idUser
                        ELSE NULL
                    END AS author_id,
                    CASE\s
                        WHEN u.userType = 'SINHVIEN' THEN sv.tenSinhVien
                        WHEN u.userType = 'NHANVIEN' THEN nv.tenNhanVien
                        ELSE NULL
                    END AS author_name,
                    CASE\s
                        WHEN u.userType = 'SINHVIEN' THEN sv.avaFileCode
                        WHEN u.userType = 'NHANVIEN' THEN nv.avaFileCode
                        ELSE NULL
                    END AS author_avatar,
                   \s
                    -- Tính điểm tổng
                    (SELECT COALESCE(SUM(CASE\s
                        WHEN v.type = 'UP' THEN 1\s
                        WHEN v.type = 'DOWN' THEN -1\s
                        ELSE 0 END), 0)\s
                     FROM vote v\s
                     WHERE v.discussionId = dis.discussionId) AS score,\s
                   \s
                    -- Số lượng upvote
                    (SELECT COUNT(*)\s
                     FROM vote v\s
                     WHERE v.discussionId = dis.discussionId AND v.type = 'UP') AS upvote_count,
    
                    -- Số lượng downvote
                    (SELECT COUNT(*)\s
                     FROM vote v\s
                     WHERE v.discussionId = dis.discussionId AND v.type = 'DOWN') AS downvote_count,
    
                    -- Số lượng câu trả lời
                    (SELECT COUNT(*)\s
                     FROM answer a\s
                     WHERE a.discussionId = dis.discussionId) AS answer_count
    
            FROM discussion dis
            LEFT JOIN discussion_tag dt ON dt.discussionId = dis.discussionId
            LEFT JOIN tag t ON dt.tagId = t.tagId
    
            -- Join với bảng user
            LEFT JOIN user u ON u.userId = dis.userId
    
            -- Join thêm với bảng sinhvien và nhanvien để lấy thông tin chi tiết người dùng
            LEFT JOIN sinh_vien sv ON u.userId = sv.userId
            LEFT JOIN nhan_vien nv ON u.userId = nv.userId

    WHERE (dis.userId = :userId)
    AND (:title IS NULL OR dis.title LIKE CONCAT('%', :title, '%'))
    AND (:tagName IS NULL OR t.name LIKE CONCAT('%', :tagName, '%') OR t.name IS NULL)
    AND (:category IS NULL OR t.category = :category OR :category = '')
    ) AS temp
    """,
            nativeQuery = true,
            countQuery = """
        SELECT COUNT(DISTINCT dis.discussionId)
        FROM discussion dis
        LEFT JOIN discussion_tag dt ON dt.discussionId = dis.discussionId
        LEFT JOIN tag t ON dt.tagId = t.tagId
        WHERE (:title IS NULL OR dis.title LIKE CONCAT('%', :title, '%'))
        AND (:tagName IS NULL OR t.name LIKE CONCAT('%', :tagName, '%') OR t.name IS NULL)
        AND (:category IS NULL OR t.category = :category OR :category = '')
    """)
    Page<Object[]> findByUser_UserId(@Param("userId") Integer userId,
                                     @Param("title") String title,
                                     @Param("tagName") String tagName,
                                     @Param("category") String category,
                                     Pageable pageable);

    @Query(value = """
    SELECT * FROM (
            SELECT DISTINCT
                    dis.discussionId,\s
                    dis.title,
                    dis.content,\s
                    dis.createAt,\s
                    dis.status,
                   \s
                    -- Lấy thông tin người đăng bài
                    CASE\s
                        WHEN u.userType = 'SINHVIEN' THEN sv.maSinhVien
                        WHEN u.userType = 'NHANVIEN' THEN nv.idUser
                        ELSE NULL
                    END AS author_id,
                    CASE\s
                        WHEN u.userType = 'SINHVIEN' THEN sv.tenSinhVien
                        WHEN u.userType = 'NHANVIEN' THEN nv.tenNhanVien
                        ELSE NULL
                    END AS author_name,
                    CASE\s
                        WHEN u.userType = 'SINHVIEN' THEN sv.avaFileCode
                        WHEN u.userType = 'NHANVIEN' THEN nv.avaFileCode
                        ELSE NULL
                    END AS author_avatar,
                   \s
                    -- Tính điểm tổng
                    (SELECT COALESCE(SUM(CASE\s
                        WHEN v.type = 'UP' THEN 1\s
                        WHEN v.type = 'DOWN' THEN -1\s
                        ELSE 0 END), 0)\s
                     FROM vote v\s
                     WHERE v.discussionId = dis.discussionId) AS score,\s
                   \s
                    -- Số lượng upvote
                    (SELECT COUNT(*)\s
                     FROM vote v\s
                     WHERE v.discussionId = dis.discussionId AND v.type = 'UP') AS upvote_count,
    
                    -- Số lượng downvote
                    (SELECT COUNT(*)\s
                     FROM vote v\s
                     WHERE v.discussionId = dis.discussionId AND v.type = 'DOWN') AS downvote_count,
    
                    -- Số lượng câu trả lời
                    (SELECT COUNT(*)\s
                     FROM answer a\s
                     WHERE a.discussionId = dis.discussionId) AS answer_count
    
            FROM discussion dis
            LEFT JOIN discussion_tag dt ON dt.discussionId = dis.discussionId
            LEFT JOIN tag t ON dt.tagId = t.tagId
    
            -- Join với bảng user
            LEFT JOIN user u ON u.userId = dis.userId
    
            -- Join thêm với bảng sinhvien và nhanvien để lấy thông tin chi tiết người dùng
            LEFT JOIN sinh_vien sv ON u.userId = sv.userId
            LEFT JOIN nhan_vien nv ON u.userId = nv.userId

    WHERE (dis.status = 'APPROVED')
    AND (:title IS NULL OR dis.title LIKE CONCAT('%', :title, '%'))
    AND (:tagName IS NULL OR t.name LIKE CONCAT('%', :tagName, '%') OR t.name IS NULL)
    AND (:category IS NULL OR t.category = :category OR :category = '')
    ) AS temp
    """,
            nativeQuery = true,
            countQuery = """
        SELECT COUNT(DISTINCT dis.discussionId)
        FROM discussion dis
        LEFT JOIN discussion_tag dt ON dt.discussionId = dis.discussionId
        LEFT JOIN tag t ON dt.tagId = t.tagId
        WHERE (:title IS NULL OR dis.title LIKE CONCAT('%', :title, '%'))
        AND (:tagName IS NULL OR t.name LIKE CONCAT('%', :tagName, '%') OR t.name IS NULL)
        AND (:category IS NULL OR t.category = :category OR :category = '')
    """)
    Page<Object[]> findByAllCondition(@Param("title") String title,
                                      @Param("tagName") String tagName,
                                      @Param("category") String category,
                                      Pageable pageable);

    boolean existsByDiscussionIdAndUser_UserId(Integer discussionId, Integer userId);
}
