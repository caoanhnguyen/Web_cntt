package com.kma.repository;

import java.util.List;

import com.kma.repository.entities.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface nhanVienRepo extends JpaRepository<NhanVien, Integer> {
	List<NhanVien> findByTenNhanVienContaining(String TenNhanVien);

	// Tìm nhân viên theo tên và môn học
	//JPQL
	@Query("SELECT nv FROM NhanVien nv JOIN nv.monHocList mh WHERE nv.tenNhanVien LIKE %:name% AND mh.tenMonHoc LIKE %:tenMonHoc%")
	List<NhanVien> findByNameAndMonHoc(@Param("name") String name, @Param("tenMonHoc") String tenMonHoc);
}
