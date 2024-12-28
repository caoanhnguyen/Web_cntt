package com.kma.repository;

import com.kma.repository.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userRepo extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String userName);

    @Query(value = """
    SELECT\s
        u.userId AS accountId,
        u.userName AS userName,
        u.isLocked AS isLocked,
       \s
        -- Lấy entityId
        nv.maNhanVien AS entityId,
       \s
        -- Lấy fullName
        nv.tenNhanVien AS fullName

    FROM user u
    LEFT JOIN nhan_vien nv ON u.userId = nv.userId
    WHERE u.userType = 'NHANVIEN'
      AND (:searchTerm IS NULL OR nv.maNhanVien LIKE CONCAT('%', :searchTerm, '%') OR nv.tenNhanVien LIKE CONCAT('%', :searchTerm, '%'))
   \s""", nativeQuery = true)
    Page<Object[]> findUser_NhanVienDetails(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query(value = """
    SELECT\s
        u.userId AS accountId,
        u.userName AS userName,
        u.isLocked AS isLocked,
       \s
        -- Lấy entityId
        sv.maSinhVien AS entityId,
       \s
        -- Lấy fullName
        sv.tenSinhVien AS fullName

    FROM user u
    LEFT JOIN sinh_vien sv ON u.userId = sv.userId
    WHERE u.userType = 'SINHVIEN'
      AND (:searchTerm IS NULL OR sv.maSinhVien LIKE CONCAT('%', :searchTerm, '%') OR sv.tenSinhVien LIKE CONCAT('%', :searchTerm, '%'))
   \s""", nativeQuery = true)
    Page<Object[]> findUser_SinhVienDetails(@Param("searchTerm") String searchTerm, Pageable pageable);
}
