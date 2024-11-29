package com.kma.services.Impl;

import com.kma.constants.fileDirection;
import com.kma.converter.sinhVienDTOConverter;
import com.kma.converter.suKienDTOConverter;
import com.kma.models.paginationResponseDTO;
import com.kma.models.sinhVienDTO;
import com.kma.models.sinhVienResponseDTO;
import com.kma.models.suKienResponseDTO;
import com.kma.repository.entities.DangKySuKien;
import com.kma.repository.entities.SinhVien;
import com.kma.repository.sinhVienRepo;
import com.kma.services.fileService;
import com.kma.services.sinhVienService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class sinhVienServImpl implements sinhVienService {
    @Autowired
    sinhVienRepo svRepo;
    @Autowired
    sinhVienDTOConverter svDTOConverter;
    @Autowired
    fileService fileServ;
    @Autowired
    suKienDTOConverter skDTOConverter;


    @Override
    public paginationResponseDTO<sinhVienResponseDTO> getAllSinhVien(Map<String, Object> params, Integer page, Integer size) {
        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu từ repository
        Page<SinhVien> svPage = fetchSinhViens(params, pageable);

        // Chuyển đổi sang DTO
        List<sinhVienResponseDTO> svResDTOList = svPage.getContent().stream()
                .map(svDTOConverter::convertToSVResDTO)
                .toList();

        // Đóng gói dữ liệu và meta vào DTO
        return new paginationResponseDTO<>(
                svResDTOList,
                svPage.getTotalPages(),
                (int) svPage.getTotalElements(),
                svPage.isFirst(),
                svPage.isLast(),
                svPage.getNumber(),
                svPage.getSize()
        );
    }

    @Override
    public List<suKienResponseDTO> getAllParicipatedEvent(String maSinhVien) {
        // Kiểm tra sinh viên có tồn tại không
        SinhVien sv = svRepo.findById(maSinhVien).orElse(null);

        if(sv!=null){
            List<DangKySuKien> dkskList = sv.getDkskList();
            return dkskList.stream()
                    .map(i->(skDTOConverter.convertToSKResDTO(i.getEvent())))
                    .toList();
        }else{
            throw new EntityNotFoundException("Student not found with id: " + maSinhVien);
        }
    }

    private Page<SinhVien> fetchSinhViens(@NotNull Map<String, Object> params, Pageable pageable){
        // Lấy giá trị từ params
        String maSinhVien = (params.get("maSinhVien") != null ? (String) params.get("maSinhVien") : "");
        String tenSinhVien = (params.get("tenSinhVien") != null ? (String) params.get("tenSinhVien") : "");
        String tenLop = (params.get("tenLop") != null ? (String) params.get("tenLop") : "");

        return svRepo.findByAllCondition(tenSinhVien, maSinhVien, tenLop, pageable);
    }

    @Override
    public sinhVienDTO findById(String maSinhVien) {
        SinhVien sv = svRepo.findById(maSinhVien).orElse(null);
        if(sv!=null)
            return svDTOConverter.convertToSVDTO(sv);
        throw new EntityNotFoundException("Student not found with id: " + maSinhVien);
    }

    @Override
    public void addSinhVien(MultipartFile file, sinhVienDTO svDTO) throws IOException {
        // Kiểm tra mã sinh viên
        SinhVien svien = svRepo.findById(svDTO.getMaSinhVien()).orElse(null);
        if(svien==null){

            // Lưu avaFile, lấy avaFileCode
            String avaFileCode = "";
            if(file!=null){
                String fileDirec = fileDirection.pathForProfile_SV + "/" + svDTO.getTenLop() + "/" + svDTO.getMaSinhVien();
                avaFileCode = fileServ.uploadFile(file, fileDirec);
            }

            // Tạo sinh viên để lưu
            SinhVien sv = svDTOConverter.convertToSV(svDTO, avaFileCode);
            svRepo.save(sv);

        }else{
            throw new EntityNotFoundException("Mã sinh viên đã tồn tại, vui lòng kiểm tra lại!");
        }

    }

    @Transactional
    @Override
    public void updateSinhVien(String maSinhVien, sinhVienDTO svDTO, MultipartFile file) throws IOException {
        SinhVien sv = svRepo.findById(maSinhVien).orElse(null);
        if(sv != null){
            String avaFileCode = sv.getAvaFileCode();
            if(file!=null){
                // Xử lí xóa bỏ file cũ
                fileServ.deleteFile(maSinhVien, 3);

                // Xử lí lưu avaFile mới
                String fileDirec = fileDirection.pathForProfile_SV + "/" + svDTO.getTenLop() + "/" + svDTO.getMaSinhVien();
                avaFileCode = fileServ.uploadFile(file, fileDirec);
            }
            // Update dữ liệu
            sv = svDTOConverter.convertToSV(svDTO, avaFileCode);
            svRepo.save(sv);
        }else{
            throw new EntityNotFoundException("Student not found with id: " + maSinhVien);
        }
    }

    @Override
    public void deleteSinhVien(String maSinhVien) {
        SinhVien sv = svRepo.findById(maSinhVien).orElse(null);
        if(sv != null){
            // Xử lí xóa bỏ profile
            fileServ.deleteFile(maSinhVien, 3);
            svRepo.delete(sv);
        }else{
            throw new EntityNotFoundException("Student not found with id: " + maSinhVien);
        }
    }
}
