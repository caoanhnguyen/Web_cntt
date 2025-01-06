package com.kma.services.Impl;

import com.kma.converter.menuItemDTOConverter;
import com.kma.models.menuItemReqDTO;
import com.kma.models.menuItemResDTO;
import com.kma.models.navBarDTO;
import com.kma.repository.entities.MenuItem;
import com.kma.repository.menuItemRepo;
import com.kma.services.menuItemService;
import com.kma.utilities.SlugUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class menuItemServImpl implements menuItemService {
    @Autowired
    menuItemRepo menuIRepo;
    @Autowired
    menuItemDTOConverter menuItemDTOConverter;

    @Override
    public List<navBarDTO> getAllMenuItem() {
        List<MenuItem> rootMenuItems = menuIRepo.findRootMenuItems();
        return menuItemDTOConverter.mapToTree(rootMenuItems);
    }

    @Override
    public void addMenuItem(menuItemReqDTO menuItemReqDTO) {
        // Kiểm tra parent có tồn tại không
        if(!menuIRepo.existsById(menuItemReqDTO.getParentId()))
            throw new EntityNotFoundException("Parent menu item not found!");

        // Tạo menuItem mới
        MenuItem menuItem = new MenuItem();
        menuItem.setTitle(menuItemReqDTO.getTitle());
        String slug = SlugUtils.toSlug(menuItemReqDTO.getTitle());
        menuItem.setSlug(slug);
        menuItem.setParentId(menuItemReqDTO.getParentId());

        menuIRepo.save(menuItem);
    }

    @Override
    public void updateMenuItem(Integer menuItemId, menuItemResDTO menuItemResDTO) {
        // Kiểm tra có tồn tại không
        MenuItem menuItem = menuIRepo.findById(menuItemId).orElse(null);
        if(menuItem == null)
            throw new EntityNotFoundException("Menu item not found!");

        // Kiểm tra parent có tồn tại không
        if(!menuIRepo.existsById(menuItemResDTO.getParentId()))
            throw new EntityNotFoundException("Parent menu item not found!");

        menuItem.setTitle(menuItemResDTO.getTitle());
        menuItem.setSlug(menuItemResDTO.getSlug());
        menuItem.setParentId(menuItemResDTO.getParentId());

        menuIRepo.save(menuItem);

    }

    @Override
    public void softDeleteMenuItem(Integer menuItemId) {
        // Kiểm tra có tồn tại không
        MenuItem menuItem = menuIRepo.findById(menuItemId).orElse(null);
        if(menuItem == null)
            throw new EntityNotFoundException("Menu item not found!");

        // Soft delete
        menuItem.setDeleted(true);
        menuIRepo.save(menuItem);
    }

    @Override
    public void restoreMenuItem(Integer menuItemId) {
        // Kiểm tra có tồn tại không
        MenuItem menuItem = menuIRepo.findById(menuItemId).orElse(null);
        if(menuItem == null)
            throw new EntityNotFoundException("Menu item not found!");

        // Soft delete
        menuItem.setDeleted(false);
        menuIRepo.save(menuItem);
    }
}
