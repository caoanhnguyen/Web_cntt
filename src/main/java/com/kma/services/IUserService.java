package com.kma.services;

import com.kma.models.changePasswordDTO;

public interface IUserService {
    String login(String userName, String password) throws Exception;

    void changePassword(changePasswordDTO changePasswordDTO) throws Exception;

    void resetPasswordForUser(Integer userId) throws Exception;

    void addRole(Integer accountId, Integer roleId) throws Exception;

}
