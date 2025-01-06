package com.kma.services;

import com.kma.models.menuItemReqDTO;
import com.kma.models.menuItemResDTO;
import com.kma.models.navBarDTO;

import java.util.List;

public interface menuItemService {

    List<navBarDTO> getAllMenuItem();

    void addMenuItem(menuItemReqDTO menuItemReqDTO);

    void updateMenuItem(Integer menuItemId, menuItemResDTO menuItemResDTO);

    void softDeleteMenuItem(Integer menuItemId);

    void restoreMenuItem(Integer menuItemId);
}
