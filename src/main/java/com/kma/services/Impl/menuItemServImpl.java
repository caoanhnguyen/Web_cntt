package com.kma.services.Impl;

import com.kma.converter.menuItemDTOConverter;
import com.kma.models.menuItemReqDTO;
import com.kma.models.menuItemResDTO;
import com.kma.models.navBarDTO;
import com.kma.repository.entities.MenuItem;
import com.kma.repository.menuItemRepo;
import com.kma.services.menuItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class menuItemServImpl implements menuItemService {
    @Autowired
    menuItemRepo menuIRepo;
    @Autowired
    menuItemDTOConverter menuItemDTOConverter;

    @Override
    public List<navBarDTO> getAllMenuItemForAdmin(Map<String, Object> params) {
        // Lấy giá trị từ params
        String title = ( params.get("title") != null ? (String) params.get("title") : "");
        Boolean isDeleted = ( params.get("isDeleted") != null ? Boolean.valueOf(params.get("isDeleted").toString()) : null);

        List<MenuItem> rootMenuItems = menuIRepo.findRootMenuItemsForAdmin(title, isDeleted);
        return menuItemDTOConverter.mapToTree(rootMenuItems); // Bao gồm cả mục bị ẩn
    }

    @Override
    public List<navBarDTO> getAllMenuItem() {
        List<MenuItem> menuItems = menuIRepo.findRootMenuItems();
        return menuItemDTOConverter.mapToTree(menuItems); // Loại bỏ mục bị ẩn
    }

    @Override
    public void addMenuItem(menuItemReqDTO menuItemReqDTO) {
        // Kiểm tra parent có tồn tại không
        if(menuItemReqDTO.getParentId()!=null && !menuIRepo.existsById(menuItemReqDTO.getParentId()))
            throw new EntityNotFoundException("Parent menu item not found!");

        // Tạo menuItem mới
        MenuItem menuItem = new MenuItem();
        menuItem.setTitle(menuItemReqDTO.getTitle());
        menuItem.setSlug(menuItemReqDTO.getSlug());
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
    public void updateMenuItemStatus(Integer menuItemId, Boolean statusUpdate) {
        // Kiểm tra có tồn tại không
        MenuItem menuItem = menuIRepo.findById(menuItemId).orElse(null);
        if(menuItem == null)
            throw new EntityNotFoundException("Menu item not found!");

        menuItem.setDeleted(statusUpdate);
        menuIRepo.save(menuItem);
    }

    @Override
    public void deleteMenuItem(Integer menuItemId) {
        // Kiểm tra có tồn tại không
        MenuItem menuItem = menuIRepo.findById(menuItemId).orElse(null);
        if(menuItem == null)
            throw new EntityNotFoundException("Menu item not found!");

        // Soft delete
        menuIRepo.delete(menuItem);
    }
}
