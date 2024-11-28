package com.kma.services.Impl;

import com.kma.models.registrationDTO;
import com.kma.repository.dkSuKienRepo;
import com.kma.repository.entities.DangKySuKien;
import com.kma.repository.entities.SinhVien;
import com.kma.repository.entities.SuKien;
import com.kma.repository.sinhVienRepo;
import com.kma.repository.suKienRepo;
import com.kma.services.dangKySKService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@Transactional
public class dangKySKServImpl implements dangKySKService {

    @Autowired
    sinhVienRepo svRepo;
    @Autowired
    suKienRepo skRepo;
    @Autowired
    dkSuKienRepo dkskRepo;

    @Override
    public void eventRegistration(registrationDTO regisDTO) {
        // Kiểm tra sinh viên và sự kiện hợp lệ
        SinhVien sv = svRepo.findById(regisDTO.getMaSinhVien()).orElse(null);
        SuKien event = skRepo.findById(regisDTO.getEventId()).orElse(null);

        if(sv != null && event!=null){
            // Nếu valid thì tạo bản ghi đăng kí sự kiện
            DangKySuKien dksk = new DangKySuKien();

            dksk.setEvent(event);
            dksk.setSinhVien(sv);
            dksk.setRegisDate(new Date(System.currentTimeMillis()));
            dkskRepo.save(dksk);

        }else{
            throw new EntityNotFoundException("Student or event not found!");
        }
    }
}
