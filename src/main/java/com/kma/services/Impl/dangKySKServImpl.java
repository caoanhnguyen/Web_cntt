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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

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
    public SuKien getEventById(Integer eventId) {
        return skRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Override
    public byte[] exportDanhSachSinhVien(Integer eventId) throws IOException {
        // Lấy thông tin sự kiện
        SuKien event = getEventById(eventId);
        List<DangKySuKien> danhSachDangKy = dkskRepo.findByEventId(eventId);

        // Tạo workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Danh sách sinh viên");

        // Thêm thông tin sự kiện
        int rowNum = 0;

        XSSFRow eventInfoRow1 = sheet.createRow(rowNum++);
        assert event != null;
        eventInfoRow1.createCell(0).setCellValue("Tên sự kiện: " + event.getEventName());

        XSSFRow eventInfoRow2 = sheet.createRow(rowNum++);
        eventInfoRow2.createCell(0).setCellValue("Thời gian: " + event.getStartAt() + " - " + event.getEndAt());

        XSSFRow eventInfoRow3 = sheet.createRow(rowNum++);
        eventInfoRow3.createCell(0).setCellValue("Địa điểm: " + event.getLocation());

        XSSFRow eventInfoRow4 = sheet.createRow(rowNum++);
        eventInfoRow4.createCell(0).setCellValue("Đơn vị tổ chức: " + event.getOrganizedBy());

        // Thêm khoảng cách trước bảng
        rowNum++;

        // Tạo header
        String[] headers = {
                "STT", "Mã Sinh Viên", "Tên Sinh Viên", "Giới Tính", "Ngày Sinh",
                "Điện Thoại", "Email", "Ngày Đăng Ký", "Trạng Thái"
        };

        XSSFRow headerRow = sheet.createRow(rowNum++);
        for (int i = 0; i < headers.length; i++) {
            XSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Thêm dữ liệu
        int stt = 1;
        for (DangKySuKien dksk : danhSachDangKy) {
            SinhVien sinhVien = dksk.getSinhVien();
            XSSFRow row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(stt++); // STT
            row.createCell(1).setCellValue(sinhVien.getMaSinhVien());
            row.createCell(2).setCellValue(sinhVien.getTenSinhVien());
            row.createCell(3).setCellValue(sinhVien.getGioiTinh().toString());
            row.createCell(4).setCellValue(sinhVien.getNgaySinh() != null ? sinhVien.getNgaySinh().toString() : "");
            row.createCell(5).setCellValue(sinhVien.getDienThoai());
            row.createCell(6).setCellValue(sinhVien.getEmail());
            row.createCell(7).setCellValue(dksk.getRegisDate() != null ? dksk.getRegisDate().toString() : "");
//            row.createCell(8).setCellValue(getStatusLabel(dksk.getStatus()));
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi workbook vào byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

//    // Chuyển trạng thái thành chuỗi
//    private String getStatusLabel(Integer status) {
//        return switch (status) {
//            case 0 -> "Chờ Xác Nhận";
//            case 1 -> "Đã Xác Nhận";
//            case 2 -> "Hủy";
//            default -> "Không Xác Định";
//        };
//    }

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
