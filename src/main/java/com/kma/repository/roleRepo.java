package com.kma.repository;

import com.kma.repository.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface roleRepo extends JpaRepository<Role, Integer> {
    Role findByRoleName(String roleName);
}
