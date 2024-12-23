package com.kma.services;

import com.kma.enums.SubjectCategory;
import com.kma.models.monHocDTO;
import com.kma.models.monHocResponseDTO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface monHocService {

    Map<SubjectCategory, List<monHocResponseDTO>> getGroupedSubjects();

    monHocDTO getById(Integer idMonHoc);

    List<monHocDTO> getAllMonHoc(@RequestParam Map<Object,Object> params);

    void addMonHoc(List<MultipartFile> files,
                   monHocDTO mhDTO) throws IOException;

    void updateMonHoc(Integer idMonHoc,
                    monHocDTO monHocDTO,
                    List<MultipartFile> files,
                    List<Integer> deleteFileIds) throws IOException;

    void deleteMonHoc(Integer idMonHoc);
}
