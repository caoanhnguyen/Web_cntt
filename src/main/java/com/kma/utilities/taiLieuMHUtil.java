package com.kma.utilities;

import com.kma.models.monHocDTO;
import com.kma.repository.entities.MonHoc;
import com.kma.repository.entities.TaiLieuMonHoc;

import java.sql.Date;

public class taiLieuMHUtil {
    public static TaiLieuMonHoc createDoc(String fileCode, MonHoc monHoc) {
        try {
            TaiLieuMonHoc tlmh = new TaiLieuMonHoc();
            tlmh.setDescription(monHoc.getTenMonHoc());
            tlmh.setCreateAt(new Date(System.currentTimeMillis()));
            tlmh.setFileCode(fileCode);
            tlmh.setMonHoc(monHoc);

            return tlmh;
        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }
    }
}
