package com.kma.repository;

import com.kma.repository.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userRepo extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);

}
