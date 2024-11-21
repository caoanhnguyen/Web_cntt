package com.kma.services;


import com.kma.models.monHocDTO;
import com.kma.models.postRequestDTO;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface monHocService {
    List<monHocDTO> getAllMonHoc(@RequestParam Map<Object,Object> params);

    void addMonHoc(@RequestParam(value = "file", required = false) List<MultipartFile> files,
                   @ModelAttribute monHocDTO mhDTO) throws IOException;

    void updateMonHoc(@PathVariable Integer idMonHoc,
                    @ModelAttribute monHocDTO monHocDTO,
                    @RequestParam(value = "files", required = false) List<MultipartFile> files,
                    @RequestParam(value = "deleteFiles", required = false) List<Integer> deleteFileIds) throws IOException;

    void deleteMonHoc(Integer idMonHoc);
}
