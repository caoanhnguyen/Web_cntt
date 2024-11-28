package com.kma.repository;

import com.kma.repository.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userAccRepo extends JpaRepository<UserAccount, Integer> {
    UserAccount findByUserName(String userName);
}
