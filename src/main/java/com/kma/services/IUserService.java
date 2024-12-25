package com.kma.services;

import com.kma.models.accountDTO;
import com.kma.models.changePasswordDTO;
import com.kma.models.paginationResponseDTO;


public interface IUserService {
    String login(String userName, String password) throws Exception;

    void changePassword(changePasswordDTO changePasswordDTO) throws Exception;

    void resetPasswordForUser(Integer userId) throws Exception;

    void addRole(Integer accountId, Integer roleId) throws Exception;

    void removeRole(Integer accountId, Integer roleId) throws Exception;

    void updateAccountLockStatus(Integer userId, boolean isLocked);

    paginationResponseDTO<accountDTO> getAllUser_NhanVienAccount(String searchTerm, Integer page, Integer size);

    paginationResponseDTO<accountDTO> getAllUser_SinhVienAccount(String searchTerm, Integer page, Integer size);

}
