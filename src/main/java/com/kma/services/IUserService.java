package com.kma.services;

import com.kma.models.changePasswordDTO;

public interface IUserService {
    String login(String userName, String password) throws Exception;

    void changePassword(String username, changePasswordDTO changePasswordDTO) throws Exception;
}
