package com.kma.services.Impl;

import com.kma.converter.sliderDTOConverter;
import com.kma.models.paginationResponseDTO;
import com.kma.models.sliderDTO;
import com.kma.repository.entities.Slider;
import com.kma.repository.sliderRepo;
import com.kma.services.sliderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class sliderServImpl implements sliderService {
    @Autowired
    sliderRepo sliderRepo;
    @Autowired
    sliderDTOConverter sliderConverter;

    @Override
    public List<sliderDTO> getLatestSlider() {
        List<Slider> sliderDTOList = sliderRepo.findTop6ByOrderByCreateAtDesc();
        return sliderDTOList.stream()
                .map(i->sliderConverter.convertToSliderDTO(i))
                .collect(Collectors.toList());
    }

    @Override
    public sliderDTO getById(Integer sliderId) {
        // Kiểm tra xem slider có tồn tại không
        Slider slider = sliderRepo.findById(sliderId).orElse(null);

        if(slider !=null){
            return sliderConverter.convertToSliderDTO(slider);
        }else{
            throw new EntityNotFoundException("Slider not found!");
        }
    }

    @Override
    public paginationResponseDTO<sliderDTO> getAllSlider(Integer page, Integer size, String sort, String order) {
        // Lấy sortCriterial
        Sort sortCriteria = handleSort(sort,order);

        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size, sortCriteria);

        // Lấy dữ liệu từ repository
        Page<Slider> sliderPage = sliderRepo.getAllSlider(pageable);

        // Chuyển đổi Post sang postResponseDTO
        List<sliderDTO> discussResDTOList = sliderPage.getContent().stream()
                .map(sliderConverter::convertToSliderDTO)
                .toList();

        // Đóng gói dữ liệu và meta vào DTO
        return new paginationResponseDTO<>(
                discussResDTOList,
                sliderPage.getTotalPages(),
                (int) sliderPage.getTotalElements(),
                sliderPage.isFirst(),
                sliderPage.isLast(),
                sliderPage.getNumber(),
                sliderPage.getSize()
        );
    }



    private Sort handleSort(String sort, String order) {
        // Xử lý Sort theo tiêu chí và thứ tự
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Xác định trường sắp xếp dựa trên sort
        return switch (sort) {
            case "score" ->  // Sắp xếp theo điểm
                    Sort.by(direction, "score");
            case "answers" ->  // Sắp xếp theo số câu trả lời
                    Sort.by(direction, "answer_count");  // Sắp xếp theo ngày tạo
            default -> Sort.by(direction, "createAt");
        };
    }

    @Override
    public void addSlider(sliderDTO DTO) {
        Slider slider = new Slider();
        slider.setTitle(DTO.getTitle());
        slider.setContent(DTO.getContent());
        slider.setCreateAt(new Date(System.currentTimeMillis()));

        sliderRepo.save(slider);
    }

    @Override
    public void updateSlider(Integer sliderId, sliderDTO DTO) {
        // Kiểm tra xem slider có tồn tại không
        Slider slider = sliderRepo.findById(sliderId).orElse(null);

        if(slider !=null){
            slider.setTitle(DTO.getTitle());
            slider.setContent(DTO.getContent());
            slider.setCreateAt(new Date(System.currentTimeMillis()));
        }else{
            throw new EntityNotFoundException("Slider not found!");
        }
        sliderRepo.save(slider);
    }

    @Override
    public void deleteSlider(Integer sliderId) {
        // Kiểm tra xem slider có tồn tại không, có thì xóa
        if(sliderRepo.findById(sliderId).isPresent()){
            sliderRepo.deleteById(sliderId);
        }else{
            throw new EntityNotFoundException("Slider not found!");
        }
    }
}
