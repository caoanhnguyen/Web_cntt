package com.kma.repository;

import com.kma.repository.entities.NhanVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface nhanVienRepo extends JpaRepository<NhanVien, Integer> {

	boolean existsByMaNhanVien(String maNhanVien);

	NhanVien findByAvaFileCode(String avaFileCode);

//	NhanVien findByMaNhanVien(String maNhanVien);

	// Tìm nhân viên theo tên và môn học
	//JPQL
	@Query("SELECT nv FROM NhanVien nv " +
			"LEFT JOIN nv.monHocList mh " +
			"LEFT JOIN nv.phongBan pb " +
			"WHERE (:tenNhanVien IS NULL OR nv.tenNhanVien LIKE %:tenNhanVien%) " +
			"AND (:tenMonHoc IS NULL OR mh.tenMonHoc LIKE %:tenMonHoc% OR mh IS NULL) " +
			"AND (:tenPhongBan IS NULL OR pb.tenPhongBan LIKE %:tenPhongBan% OR pb.maPhongBan IS NULL OR pb.maPhongBan LIKE %:tenPhongBan%) " +
			"ORDER BY nv.idUser ASC, SUBSTRING_INDEX(nv.tenNhanVien, ' ', -1) ASC")
	Page<NhanVien> findByAllCondition(@Param("tenNhanVien") String tenNhanVien,
									  @Param("tenMonHoc") String tenMonHoc,
									  @Param("tenPhongBan") String tenPhongBan,
									  Pageable pageable);


}
