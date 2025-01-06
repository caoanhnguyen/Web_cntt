package com.kma.converter;

import com.kma.models.navBarDTO;
import com.kma.repository.entities.MenuItem;
import com.kma.repository.menuItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class menuItemDTOConverter {
    @Autowired
    menuItemRepo menuItemRepo;

    public List<navBarDTO> mapToTree(List<MenuItem> menuItems) {
        return menuItems.stream()
                .filter(menuItem -> !menuItem.getDeleted()) // Loại bỏ mục đã xóa
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private navBarDTO mapToDTO(MenuItem menuItem) {
        navBarDTO dto = new navBarDTO();
        dto.setId(menuItem.getId());
        dto.setTitle(menuItem.getTitle());
        dto.setSlug(menuItem.getSlug());
        dto.setChildren(mapToTree(menuItemRepo.findChildren(menuItem.getId())));
        return dto;
    }
}
