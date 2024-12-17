package com.kma.utilities;

import com.kma.models.entityInfo;
import com.kma.models.userDTO;
import com.kma.repository.entities.NhanVien;
import com.kma.repository.entities.SinhVien;
import com.kma.repository.entities.User;
import com.kma.repository.nhanVienRepo;
import com.kma.repository.sinhVienRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class userInfoUtil {

    @Autowired
    nhanVienRepo nvRepo;
    @Autowired
    sinhVienRepo svRepo;

    public entityInfo getInfoOfEntity(User user){
        String userName = user.getUserName();
        String avaFileCode = "/downloadProfile/";
        Object entityId = null;
        NhanVien nv = nvRepo.findByMaNhanVien(userName);
        if(nv!=null){
            avaFileCode += nv.getAvaFileCode();
            entityId = nv.getIdUser();
        }else{
            SinhVien sv = svRepo.findById(userName).orElse(null);
            avaFileCode += Objects.requireNonNull(sv).getAvaFileCode();
            entityId = sv.getMaSinhVien();
        }
        return new entityInfo(entityId, avaFileCode);
    }

    public userDTO getUserInfo(User user){
        String userName = user.getUserName();
        String avaFileCode = "/downloadProfile/";
        Object entityId = null;
        String name;
        NhanVien nv = nvRepo.findByMaNhanVien(userName);
        if(nv!=null){
            name = nv.getTenNhanVien();
            avaFileCode += nv.getAvaFileCode();
            entityId = nv.getIdUser();
        }else{
            SinhVien sv = svRepo.findById(userName).orElse(null);
            assert sv != null;
            name = sv.getTenSinhVien();
            avaFileCode += Objects.requireNonNull(sv).getAvaFileCode();
            entityId = sv.getMaSinhVien();
        }
        return new userDTO(entityId, name, avaFileCode);
    }
}
