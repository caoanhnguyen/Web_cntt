package com.kma.services.Impl;

import com.kma.constants.fileDirection;
import com.kma.converter.monHocDTOConverter;
import com.kma.enums.SubjectCategory;
import com.kma.models.monHocDTO;
import com.kma.models.monHocResponseDTO;
import com.kma.repository.entities.MonHoc;
import com.kma.repository.entities.TaiLieuMonHoc;
import com.kma.repository.monHocRepo;
import com.kma.repository.taiLieuMonHocRepo;
import com.kma.services.fileService;
import com.kma.services.monHocService;
import com.kma.utilities.stringUtil;
import com.kma.utilities.taiLieuMHUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class monHocServImpl implements monHocService {

    @Autowired
    monHocRepo mhRepo;
    @Autowired
    monHocDTOConverter mhDTOConverter;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    fileService fileServ;
    @Autowired
    taiLieuMonHocRepo tlmhRepo;

    @Override
    public Map<SubjectCategory, List<monHocResponseDTO>> getGroupedSubjects() {
        // Lấy tất cả môn học
        List<MonHoc> subjects = mhRepo.findAllByOrderByCategory();

        // Nhóm các môn học theo danh mục
        Map<SubjectCategory, List<monHocResponseDTO>> groupedSubjects = new LinkedHashMap<>();
        for (SubjectCategory category : SubjectCategory.values()) {
            List<monHocResponseDTO> subjectDTOs = subjects.stream()
                    .filter(subject -> subject.getCategory() == category)
                    .map(mhDTOConverter::convertToMonHocResDTO)
                    .toList();
            groupedSubjects.put(category, subjectDTOs);
        }
        return groupedSubjects;
    }

    @Override
    public monHocDTO getById(Integer idMonHoc) {
        MonHoc monHoc = mhRepo.findById(idMonHoc).orElse(null);
        if(monHoc!=null)
            return mhDTOConverter.convertToMonHocDTO(monHoc);
        throw new EntityNotFoundException("Subject not found with id: " + idMonHoc);
    }

    @Override
    public List<monHocDTO> getAllMonHoc(Map<Object, Object> params) {
        // Lấy giá trị từ params
        String tenMonHoc = (String) params.get("tenMonHoc");
        String soTinChi = (String) params.get("soTinChi");

        // Lấy môn học theo yêu cầu
        List<MonHoc> mhList;
        if(stringUtil.checkString(tenMonHoc)){
            if(soTinChi == null){
                mhList = mhRepo.findByTenMonHocContaining(tenMonHoc);
            }else{
                mhList = mhRepo.findByTenMonHocContainingAndSoTinChi(tenMonHoc, Integer.parseInt(soTinChi));
            }
        }else{
            if(soTinChi == null){
                mhList = mhRepo.findAll();
            }else{
                mhList = mhRepo.findBySoTinChi(Integer.parseInt(soTinChi));
            }
        }
        return mhList.stream()
                     .map(mhDTOConverter::convertToMonHocDTO)
                     .toList();
    }

    @Override
    public void addMonHoc(List<MultipartFile> files, monHocDTO mhDTO) throws IOException {
        // Tạo môn học để lưu
        MonHoc monHoc = modelMapper.map(mhDTO, MonHoc.class);
        List<TaiLieuMonHoc> taiLieuMonHocList = new ArrayList<>();
        // Upload file và lấy đường dẫn nếu có
        if(files!=null){
            for (MultipartFile item: files) {
                // Lưu file và lấy fileCode
                String fileCode = fileServ.uploadFile(item, fileDirection.pathForTaiLieuMonHoc);
                // Tạo tài liệu môn học
                TaiLieuMonHoc tlmh = taiLieuMHUtil.createDoc(fileCode, monHoc);
                // Thêm tài liệu vào list tài liệu của môn học
                taiLieuMonHocList.add(tlmh);
                // Lưu tài liệu vào DB
                tlmhRepo.save(tlmh);
            }
        }
        monHoc.setTaiLieuMHList(taiLieuMonHocList);
        mhRepo.save(monHoc);
    }

    @Override
    public void updateMonHoc(Integer idMonHoc, monHocDTO monHocDTO,
                             List<MultipartFile> files,
                             List<Integer> deleteFileIds) throws IOException {
        // TODO Auto-generated method stub
        MonHoc monHoc = mhRepo.findById(idMonHoc).orElse(null);

        if(monHoc != null) {
            monHoc.setTenMonHoc(monHocDTO.getTenMonHoc());
            monHoc.setMoTa(monHocDTO.getMoTa());
            monHoc.setSoTinChi(monHocDTO.getSoTinChi());
            monHoc.setCategory(monHocDTO.getCategory());
            List<TaiLieuMonHoc> tnList = monHoc.getTaiLieuMHList();

            //Xử lí file thêm mới
            if(files != null && !files.isEmpty()) {
                for(MultipartFile file: files) {
                    // Lưu file và lấy fileCode
                    String fileCode = fileServ.uploadFile(file, fileDirection.pathForTaiLieuMonHoc);
                    // Tạo tài liệu
                    TaiLieuMonHoc taiLieuMonHoc = taiLieuMHUtil.createDoc(fileCode, monHoc);
                    // Thêm tài liệu vào list tài liệu của môn học
                    tnList.add(taiLieuMonHoc);
                    // Lưu tài liệu vào DB
                    tlmhRepo.save(taiLieuMonHoc);
                }
            }
            //Xử lí các file cần xóa
            if (deleteFileIds != null) {
                for (Integer fileId : deleteFileIds) {
                    TaiLieuMonHoc tlmh = tlmhRepo.findById(fileId).orElse(null);
                    if(tlmh != null) {
                        // Xóa file trên server, xóa tài nguyên khỏi list tài nguyên của bài viết và xóa bản ghi tài nguyên
                        fileServ.deleteFile(tlmh.getDocId(), 2);
                        tnList.remove(tlmh);
                        tlmhRepo.delete(tlmh);
                    }
                }
            }
            monHoc.setTaiLieuMHList(tnList);
            mhRepo.save(monHoc);
        }else {
            throw new EntityNotFoundException("Subject not found with id: " + idMonHoc);
        }
    }

    @Override
    public void deleteMonHoc(Integer idMonHoc) {
        // TODO Auto-generated method stub
        MonHoc existedMonHoc = mhRepo.findById(idMonHoc).orElse(null);
        if(existedMonHoc != null) {
            List<TaiLieuMonHoc> tnlist = existedMonHoc.getTaiLieuMHList();
            // Xóa hết các tài nguyên liên quan đến bài viết
            for(TaiLieuMonHoc tn: tnlist) {
                fileServ.deleteFile(tn.getDocId(), 2);
            }
            mhRepo.delete(existedMonHoc);
        }else {
            throw new EntityNotFoundException("Post not found with id: " + idMonHoc);
        }
    }
}
