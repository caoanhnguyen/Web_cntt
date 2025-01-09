package com.kma.services;

import com.kma.models.menuItemReqDTO;
import com.kma.models.menuItemResDTO;
import com.kma.models.navBarDTO;

import java.util.List;
import java.util.Map;

public interface menuItemService {

    List<navBarDTO> getAllMenuItemForAdmin(Map<String, Object> params);

    List<navBarDTO> getAllMenuItem();

    void addMenuItem(menuItemReqDTO menuItemReqDTO);

    void updateMenuItem(Integer menuItemId, menuItemResDTO menuItemResDTO);

    void updateMenuItemStatus(Integer menuItemId, Boolean statusUpdate);

    void deleteMenuItem(Integer menuItemId);
}
