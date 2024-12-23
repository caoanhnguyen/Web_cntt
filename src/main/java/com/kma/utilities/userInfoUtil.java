package com.kma.utilities;

import com.kma.enums.UserType;
import com.kma.models.userDTO;
import com.kma.repository.entities.NhanVien;
import com.kma.repository.entities.SinhVien;
import com.kma.repository.entities.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class userInfoUtil {

    public userDTO getInfoOfUser(User user) {
        // Tạo đối tượng chứa thông tin trả về
        userDTO info = new userDTO();

        // Lấy thông tin cơ bản của User
        String avaFileCode = "/downloadProfile/";

        // Kiểm tra UserType và xử lý thông tin tương ứng
        if (user.getUserType().equals(UserType.NHANVIEN)) {
            NhanVien nv = user.getNhanVien();
            if (nv != null) {
                // Nếu là nhân viên, lấy thông tin liên quan
                info.setUserId(nv.getIdUser()); // ID của nhân viên
                info.setName(nv.getTenNhanVien()); // Tên nhân viên
                avaFileCode += nv.getAvaFileCode(); // Avatar của nhân viên
            } else {
                throw new EntityNotFoundException("Thông tin nhân viên không tồn tại.");
            }
        } else if (user.getUserType().equals(UserType.SINHVIEN)) {
            SinhVien sv = user.getSinhVien();
            if (sv != null) {
                // Nếu là sinh viên, lấy thông tin liên quan
                info.setUserId(sv.getMaSinhVien()); // Mã sinh viên
                info.setName(sv.getTenSinhVien()); // Tên sinh viên
                avaFileCode += sv.getAvaFileCode(); // Avatar của sinh viên
            } else {
                throw new EntityNotFoundException("Thông tin sinh viên không tồn tại.");
            }
        } else {
            throw new IllegalStateException("User không thuộc loại hợp lệ.");
        }

        // Set avatar file code vào info
        info.setAvaFileCode(avaFileCode);

        return info;
    }

}
