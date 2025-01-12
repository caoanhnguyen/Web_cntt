package com.kma.services.Impl;

import com.kma.models.CVDTO;
import com.kma.repository.CVRepo;
import com.kma.repository.entities.CV;
import com.kma.repository.entities.NhanVien;
import com.kma.repository.nhanVienRepo;
import com.kma.services.CVService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service("cvServ")
@Transactional
public class CVServImpl implements CVService {
    @Autowired
    nhanVienRepo nvRepo;
    @Autowired
    CVRepo cvRepo;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public CVDTO getByUserId(Integer userId) {
        // Kiểm tra mã nhân viên
        NhanVien nv = nvRepo.findById(userId).orElse(null);
        if(nv!=null){
            CV cv = nv.getCv();
            CVDTO dto = new CVDTO();
            if(cv!=null){
                dto.setCVId(cv.getCVId());
                dto.setContent(cv.getContent());
            }else{
                dto = null;
            }
            return dto;
        }else{
            throw new EntityNotFoundException("Employee not found!");
        }
    }

    @Override
    public void addCV(CVDTO cvDTO, Integer idUser) {
        // Kiểm tra mã nhân viên
        NhanVien nv = nvRepo.findById(idUser).orElse(null);
        if(nv!=null){
            if(nv.getCv()!=null)
                throw new EntityExistsException("This employee already have a CV!");
            CV cv = modelMapper.map(cvDTO, CV.class);
            cv.setNhanVien(nv);
            cvRepo.save(cv);
        }else{
            throw new EntityNotFoundException("Employee not found!");
        }
    }

    @Override
    public void updateCV(Integer CVId, CVDTO cvDTO) {
        // Kiểm tra xem CV có tồn tại hay không
        CV cv = cvRepo.findById(CVId).orElse(null);
        if(cv!=null){
            cv.setContent(cvDTO.getContent());
            cvRepo.save(cv);
        }else{
            throw new EntityNotFoundException("CV not found!");
        }
    }

    @Override
    @Transactional
    public void deleteCV(Integer CVId) {
        // Kiểm tra xem CV có tồn tại hay không
        CV cv = cvRepo.findById(CVId).orElse(null);
        if(cv!=null){
            cvRepo.delete(cv);
        }else{
            throw new EntityNotFoundException("CV not found!");
        }
    }

    @Override
    public boolean isOwner(Integer CVId, Integer idUser) {
        boolean isOwner = cvRepo.existsByCVIdAndNhanVien_idUser(CVId, idUser);
        if (!isOwner) {
            throw new AccessDeniedException("You do not have permission to modify this CV.");
        }
        return true;
    }
}
