package com.kma.repository.entities;

import com.kma.enums.UserType;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name="userName", unique = true)
    private String userName;

    @Column(name="password")
    private String password;

    @Column(name="isActive")
    private Integer isActive;

    @Column(name = "userType", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    // Config relation to role
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",  // Tên bảng liên kết
            joinColumns = @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "roleId", nullable = false)
    )
    private Collection<Role> roleList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Discussion> discussions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Answer> answers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Vote> votes;

    // Quan hệ 1-1 với SinhVien
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SinhVien sinhVien;

    // Quan hệ 1-1 với NhanVien
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private NhanVien nhanVien;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public SinhVien getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }

    public Set<Discussion> getDiscussions() {
        return discussions;
    }

    public void setDiscussions(Set<Discussion> discussions) {
        this.discussions = discussions;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleList.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()))
                .collect(Collectors.toList());
    }


    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Collection<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(Collection<Role> roleList) {
        this.roleList = roleList;
    }

}
