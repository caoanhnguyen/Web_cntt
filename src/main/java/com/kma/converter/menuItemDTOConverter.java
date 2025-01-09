package com.kma.converter;

import com.kma.models.navBarDTO;
import com.kma.repository.entities.MenuItem;
import com.kma.repository.menuItemRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class menuItemDTOConverter {
    @Autowired
    menuItemRepo menuItemRepo;
    @Autowired
    ModelMapper modelMapper;

    public List<navBarDTO> mapToTree(List<MenuItem> menuItems) {
        return menuItems.stream()
//                .filter(menuItem -> includeDeleted || !menuItem.getDeleted()) // Loại bỏ mục đã xóa nếu không phải admin
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private navBarDTO mapToDTO(MenuItem menuItem) {
        navBarDTO dto = modelMapper.map(menuItem, navBarDTO.class);

        // Lấy danh sách con
        List<MenuItem> children = menuItemRepo.findChildren(menuItem.getId());
        dto.setChildren(mapToTree(children));
        return dto;
    }
}
