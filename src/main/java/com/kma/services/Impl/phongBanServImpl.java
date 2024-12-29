package com.kma.services.Impl;

import com.kma.converter.phongBanDTOConverter;
import com.kma.models.paginationResponseDTO;
import com.kma.models.phongBanResponseDTO;
import com.kma.repository.entities.PhongBan;
import com.kma.repository.phongBanRepo;
import com.kma.services.phongBanService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class phongBanServImpl implements phongBanService {

    @Autowired
    phongBanRepo pbRepo;
    @Autowired
    phongBanDTOConverter pbDTOConverter;

    @Override
    public paginationResponseDTO<phongBanResponseDTO> getAllPhongBan(Map<Object, Object> params, int page, int size) {
        // Lấy giá trị từ params
        String maPhongBan = ( params.get("maPhongBan") != null ? (String) params.get("maPhongBan") : "");
        String tenPhongBan = ( params.get("tenPhongBan") != null ? (String) params.get("tenPhongBan") : "");

        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu từ repository
        Page<PhongBan> pbPage = pbRepo.findByMaPhongBanContainingAndTenPhongBanContaining(maPhongBan, tenPhongBan, pageable);

        // Chuyển đổi Post sang postResponseDTO
        List<phongBanResponseDTO> pbResDTOList = pbPage.getContent().stream()
                .map(pbDTOConverter::convertToPBResDTO)
                .toList();

        // Đóng gói dữ liệu và meta vào DTO
        return new paginationResponseDTO<>(
                pbResDTOList,
                pbPage.getTotalPages(),
                (int) pbPage.getTotalElements(),
                pbPage.isFirst(),
                pbPage.isLast(),
                pbPage.getNumber(),
                pbPage.getSize()
        );
    }

    @Override
    public phongBanResponseDTO getById(String maPhongBan) {
        // Kiểm tra phòng ban có tồn tại hay không
        PhongBan pb = pbRepo.findById(maPhongBan).orElse(null);

        if(pb!=null){
            return pbDTOConverter.convertToPBResDTO(pb);
        }else{
            throw new EntityNotFoundException("Department not found with id: " + maPhongBan);
        }
    }

    @Override
    public void addPhongBan(phongBanResponseDTO pbResDTO) {
        // Kiểm tra mã phòng ban
        if(pbRepo.existsById(pbResDTO.getMaPhongBan()))
            throw new EntityExistsException("Department with id: " + pbResDTO.getMaPhongBan() + " already exists!");
        PhongBan phongBan = new PhongBan();
        pbDTOConverter.convertToPB(pbResDTO, phongBan);
        pbRepo.save(phongBan);
    }

    @Transactional
    @Override
    public void updatePhongBan(String maPhongBan, phongBanResponseDTO pbResDTO) {
        PhongBan pb = pbRepo.findById(maPhongBan).orElse(null);
        if(pb!=null){
            // Cập nhật phòng ban để lưu
            pb.setTenPhongBan(pbResDTO.getTenPhongBan());
            pb.setGhiChu(pbResDTO.getGhiChu());
            pbRepo.save(pb);
        }else{
            throw new EntityNotFoundException("Department not found with id: " + maPhongBan);
        }
    }

    @Override
    public void deletePhongBan(String maPhongBan) {
        PhongBan pb = pbRepo.findById(maPhongBan).orElse(null);
        if(pb != null){
            pbRepo.delete(pb);
        }else{
            throw new EntityNotFoundException("Department not found with id: " + maPhongBan);
        }
    }
}
