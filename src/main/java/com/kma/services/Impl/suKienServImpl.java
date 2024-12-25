package com.kma.services.Impl;

import com.kma.constants.fileDirection;
import com.kma.converter.sinhVienDTOConverter;
import com.kma.converter.suKienDTOConverter;
import com.kma.models.paginationResponseDTO;
import com.kma.models.sinhVienResponseDTO;
import com.kma.models.suKienDTO;
import com.kma.models.suKienResponseDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    sinhVienDTOConverter svDTOConverter;
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
    public paginationResponseDTO<sinhVienResponseDTO> getAllSVInEvent(Integer eventId, Integer page, Integer size) {
        // Kiểm tra sự kiện có tồn tại không
        SuKien event = skRepo.findById(eventId).orElse(null);

        if(event!=null){
            // Tạo Pageable từ page và size
            Pageable pageable = PageRequest.of(page, size);
            List<DangKySuKien> dkskList = event.getDkskList();

            // Tính toán phân trang
            int start = pageable.getPageNumber() * pageable.getPageSize();
            int end = Math.min((start + pageable.getPageSize()), dkskList.size());

            List<sinhVienResponseDTO> content = dkskList.subList(start, end).stream()
                    .map(i -> svDTOConverter.convertToSVResDTO(i.getSinhVien()))
                    .collect(Collectors.toList());

            // Tính toán các thông tin phân trang
            int totalElements = dkskList.size();
            int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());
            boolean isFirst = pageable.getPageNumber() == 0;
            boolean isLast = pageable.getPageNumber() + 1 == totalPages;

            return new paginationResponseDTO<>(
                    content,
                    totalPages,
                    totalElements,
                    isFirst,
                    isLast,
                    pageable.getPageNumber(),
                    pageable.getPageSize()
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
        SuKien event = skDTOConverter.convertResDTOToSuKien(skResDTO);
        List<TaiNguyen> tnList = new ArrayList<>();

        // Lưu file nếu cần
        // Xử lí file thêm mới
        saveNewFile(files, event, tnList);

        event.setTaiNguyenList(tnList);
        skRepo.save(event);

        // Gửi thông báo bài viết mới
        String title = "Có sự kiện mới. Xem ngay!";
        String content = event.getEventName();
        notiServ.sendNotificationToAllUsers(title, content);
    }

    @Override
    public void updateEvent(Integer eventId, suKienResponseDTO skResDTO, List<MultipartFile> files, List<Integer> deleteFileIds) throws IOException {
        // Kiểm tra xem event có tồn tại không
        SuKien event = skRepo.findById(eventId).orElse(null);
        if(event!=null){
            // Cập nhật
            List<TaiNguyen> tnList = event.getTaiNguyenList();
            List<DangKySuKien> dkskList = event.getDkskList();

            event = skDTOConverter.convertResDTOToSuKien(skResDTO);

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
