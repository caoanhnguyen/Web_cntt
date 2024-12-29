package com.kma.services.Impl;

import com.kma.converter.lopDTOConverter;
import com.kma.models.lopDTO;
import com.kma.models.lopRequestDTO;
import com.kma.models.paginationResponseDTO;
import com.kma.repository.entities.Lop;
import com.kma.repository.entities.NhanVien;
import com.kma.repository.lopRepo;
import com.kma.repository.nhanVienRepo;
import com.kma.services.lopService;
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
public class lopServImpl implements lopService {

    @Autowired
    lopRepo loprepo;
    @Autowired
    lopDTOConverter lDTOConverter;
    @Autowired
    nhanVienRepo nvRepo;

    @Override
    public paginationResponseDTO<lopDTO> getAllClass(Map<String, Object> params, Integer page, Integer size) {
        // Lấy giá trị từ params
        String tenLop = (params.get("tenLop") != null ? (String) params.get("tenLop") : "");
        String tenChuNhiem = (params.get("tenChuNhiem") != null ? (String) params.get("tenChuNhiem") : "");

        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu từ repository
        Page<Lop> classPage = loprepo.findByAllCondition(tenLop, tenChuNhiem, pageable);

        // Convert to DTO
        List<lopDTO> lopDTOList = classPage.stream()
                                           .map(lDTOConverter::convertToLopDTO)
                                           .toList();

        // Đóng gói dữ liệu và meta vào DTO
        return new paginationResponseDTO<>(
                lopDTOList,
                classPage.getTotalPages(),
                (int) classPage.getTotalElements(),
                classPage.isFirst(),
                classPage.isLast(),
                classPage.getNumber(),
                classPage.getSize()
        );
    }

    @Override
    public lopDTO findById(Integer idLop) {
        Lop lop = loprepo.findById(idLop).orElse(null);
        if(lop!=null){
            return lDTOConverter.convertToLopDTO(lop);
        }else{
            throw new EntityNotFoundException("Class not found with id: "+ idLop);
        }
    }

    @Override
    public void addLop(lopDTO DTO) {
        String tenLop = DTO.getTenLop();
        // Kiểm tra tên lớp đã tồn tại hay chưa
        if(loprepo.existsByTenLop(tenLop))
            throw new EntityExistsException("Class name already exists!");

        Lop lop = new Lop();
        lDTOConverter.convertToLop(DTO, lop);
        loprepo.save(lop);
    }

    @Override
    public void updateLop(Integer idLop, lopRequestDTO DTO) {
        // Kiểm tra lớp có tồn tại không
        Lop lop = loprepo.findById(idLop).orElse(null);
        if(lop!=null){
            lop.setTenLop(DTO.getTenLop());
            NhanVien chuNhiem = nvRepo.findById(DTO.getIdChuNhiem()).orElse(null);
            if(chuNhiem != null){
                lop.setChuNhiem(chuNhiem);
            }else{
                throw new EntityNotFoundException("Employee not found with id: " + DTO.getIdChuNhiem());
            }
            loprepo.save(lop);
        }else{
            throw new EntityNotFoundException("Class not found with id: "+idLop);
        }
    }

    @Override
    public void deleteLop(Integer idLop) {
        // Kiểm tra lớp có tồn tại không
        Lop lop = loprepo.findById(idLop).orElse(null);
        if(lop!=null){
            loprepo.delete(lop);
        }else{
            throw new EntityNotFoundException("Class not found with id: "+idLop);
        }
    }
}
