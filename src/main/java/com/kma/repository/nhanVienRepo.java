package com.kma.repository;

import java.util.List;

import com.kma.repository.entities.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface nhanVienRepo extends JpaRepository<NhanVien, Integer> {
	List<NhanVien> findByTenNhanVienContaining(String TenNhanVien);

	NhanVien findByAvaFileCode(String avaFileCode);

	// Tìm nhân viên theo tên và môn học
	//JPQL
	@Query("SELECT nv FROM NhanVien nv " +
			"JOIN nv.monHocList mh " +
			"LEFT JOIN nv.phongBan pb " +
			"WHERE nv.tenNhanVien LIKE %:name% " +
			"AND mh.tenMonHoc LIKE %:tenMonHoc% " +
			"AND (pb.tenPhongBan LIKE %:tenPhongBan% OR pb.maPhongBan IS NULL) ")
	List<NhanVien> findByNameAndMonHoc(@Param("name") String name, @Param("tenMonHoc") String tenMonHoc, @Param("tenPhongBan") String tenPhongBan);

}
