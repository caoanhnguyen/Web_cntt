package com.kma.services;
import com.kma.models.registrationDTO;
import com.kma.repository.entities.SuKien;

import java.io.IOException;

public interface dangKySKService {
    void eventRegistration(registrationDTO regisDTO);

    byte[] exportDanhSachSinhVien(Integer eventId) throws IOException; // Xuất danh sách sinh viên

    SuKien getEventById(Integer eventId);
}
