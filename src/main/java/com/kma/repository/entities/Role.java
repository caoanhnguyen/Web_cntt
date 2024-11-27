package com.kma.repository.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Column(name="roleName")
    private String roleName;

    @Column(name="description")
    private String description;

    // Config relationto user_account
    @ManyToMany(mappedBy = "roleList")
    private List<UserAccount> accountList = new ArrayList<>();

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UserAccount> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<UserAccount> accountList) {
        this.accountList = accountList;
    }
}
