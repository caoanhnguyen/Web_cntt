package com.kma.services.Impl;

import com.kma.constants.fileDirection;
import com.kma.converter.suKienDTOConverter;
import com.kma.models.*;
import com.kma.repository.entities.*;
import com.kma.repository.suKienRepo;
import com.kma.repository.taiNguyenRepo;
import com.kma.services.fileService;
import com.kma.services.suKienService;
import com.kma.utilities.taiNguyenUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class suKienServImpl implements suKienService {

    @Autowired
    suKienRepo skRepo;
    @Autowired
    suKienDTOConverter skDTOConverter;
    @Autowired
    fileService fileServ;
    @Autowired
    taiNguyenRepo tnRepo;
    @Autowired
    NotificationService notiServ;

    @Override
    public paginationResponseDTO<suKienResponseDTO> getAllEvent(Map<String, Object> params, Integer page, Integer size) {
        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Chuyển đổi sang suKienResponseDTO
        Page<SuKien> suKienPage = fetchSuKien(params, pageable);
        List<suKienResponseDTO> skResDTOList = suKienPage.getContent()
                                                         .stream()
                                                         .map(skDTOConverter::convertToSKResDTO)
                                                         .toList();

        // Đóng gói dữ liệu và meta vào DTO
        return new paginationResponseDTO<>(
                skResDTOList,
                suKienPage.getTotalPages(),
                (int) suKienPage.getTotalElements(),
                suKienPage.isFirst(),
                suKienPage.isLast(),
                suKienPage.getNumber(),
                suKienPage.getSize()
        );
    }

    @Override
    public paginationResponseDTO<sinhVienResponseDTO> getAllSVInEvent(Integer eventId, String searchTerm, Integer page, Integer size) {
        // Kiểm tra sự kiện có tồn tại không
        SuKien event = skRepo.findById(eventId).orElse(null);

        if(event!=null){
            // Tạo Pageable từ page và size
            Pageable pageable = PageRequest.of(page, size);

            // Lấy dữ liệu từ repository
            Page<Object[]> svPage = skRepo.findSinhVienByEventId(searchTerm, eventId, pageable);

            // Chuyển đổi Post sang postResponseDTO
            List<sinhVienResponseDTO> svResDTOList = new ArrayList<>();
            for (Object[] row : svPage) {
               sinhVienResponseDTO dto = new sinhVienResponseDTO();
                dto.setMaSinhVien((String) row[0]);
                dto.setTenSinhVien((String) row[1]);
                dto.setGioiTinh((String) row[2]);
                dto.setNgaySinh((Date) row[3]);
                dto.setQueQuan((String) row[4]);
                dto.setKhoa((String) row[5]);
                dto.setTenLop((String) row[6]);
                svResDTOList.add(dto);
            }

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
        }else{
            throw new EntityNotFoundException("Event not found!");
        }
    }

    private Page<SuKien> fetchSuKien(Map<String, Object> params, Pageable pageable){
        // Lấy giá trị từ params
        String eventName = (params.get("eventName") != null ? params.get("eventName").toString() : "");
        String location = (params.get("location") != null ? params.get("location").toString() : "");
        String organizedBy = (params.get("organizedBy") != null ? params.get("organizedBy").toString() : "");

        // Lấy dữ liệu từ repository
        return skRepo.findByAllCondition(eventName, location, organizedBy, pageable);
    }

    @Override
    public suKienDTO findById(Integer eventId) {
        SuKien sk = skRepo.findById(eventId).orElse(null);
        if(sk!=null)
            return skDTOConverter.convertToSKDTO(sk);
        throw new EntityNotFoundException("Event not found with id: " + eventId);
    }

    @Override
    public void addEvent(List<MultipartFile> files, suKienResponseDTO skResDTO) throws IOException {
        // Tạo sự kiện để lưu
        SuKien event = new SuKien();
        skDTOConverter.convertResDTOToSuKien(skResDTO, event);
        List<TaiNguyen> tnList = new ArrayList<>();

        // Lưu file nếu cần
        // Xử lí file thêm mới
        saveNewFile(files, event, tnList);

        event.setTaiNguyenList(tnList);
        skRepo.save(event);

        // Gửi thông báo bài viết mới
        String title = "Có sự kiện mới. Xem ngay!";
        String content = event.getEventName();
        String url = "api/public/sukien/"+ event.getEventId();
        notiServ.sendNotificationToAllUsers(title, content, url);
    }

    @Override
    public void updateEvent(Integer eventId, suKienResponseDTO skResDTO, List<MultipartFile> files, List<Integer> deleteFileIds) throws IOException {
        // Kiểm tra xem event có tồn tại không
        SuKien event = skRepo.findById(eventId).orElse(null);
        if(event!=null){
            // Cập nhật
            List<TaiNguyen> tnList = event.getTaiNguyenList();
            List<DangKySuKien> dkskList = event.getDkskList();

            skDTOConverter.convertResDTOToSuKien(skResDTO, event);

            // Xử lí file thêm mới
            saveNewFile(files, event, tnList);

            //Xử lí các file cần xóa
            if (deleteFileIds != null) {
                for (Integer fileId : deleteFileIds) {
                    TaiNguyen tn = tnRepo.findById(fileId).orElse(null);
                    if(tn != null) {
                        // Xóa file trên server, xóa tài nguyên khỏi list tài nguyên của bài viết và xóa bản ghi tài nguyên
                        fileServ.deleteFile(tn.getResourceId(), 1);
                        tnList.remove(tn);
                        tnRepo.delete(tn);
                    }
                }
            }
            event.setTaiNguyenList(tnList);
            event.setDkskList(dkskList);
            skRepo.save(event);

        }else{
            throw new EntityNotFoundException("Event not found with id: " + eventId);
        }
    }

    private void saveNewFile(List<MultipartFile> files, SuKien event, List<TaiNguyen> tnList) throws IOException {
        if(files != null && !files.isEmpty()) {
            for(MultipartFile file: files) {
                // Lưu file và lấy fileCode
                String fileCode = fileServ.uploadFile(file, fileDirection.pathForTaiNguyen);
                // Tạo tài nguyên
                TaiNguyen resources = taiNguyenUtil.createResource(fileCode, event);
                // Thêm tài nguyên vào list tài nguyên của sự kiện
                tnList.add(resources);
                // Lưu tài nguyên vào DB
                tnRepo.save(resources);
            }
        }
    }

    @Override
    public void deleteEvent(Integer eventId) {
        SuKien event = skRepo.findById(eventId).orElse(null);
        if(event != null) {
            List<TaiNguyen> tnlist = event.getTaiNguyenList();
            // Xóa hết các tài nguyên liên quan đến bài viết
            for(TaiNguyen tn: tnlist) {
                fileServ.deleteFile(tn.getResourceId(), 1);
            }
            skRepo.delete(event);
        }else {
            throw new EntityNotFoundException("Event not found with id: " + eventId);
        }
    }
}
