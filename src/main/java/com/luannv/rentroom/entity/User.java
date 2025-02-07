package com.luannv.rentroom.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "username", unique = true)
    @NotEmpty(message = "Username can not be blank.")
    private String username;
    @Column(name = "first_name")
    @NotEmpty(message = "First Name can not be blank.")
    private String firstName;
    @NotEmpty(message = "Last Name can not be blank.")
    @Column(name = "last_name")
    private String lastName;
    @Email(message = "Email invalid.")
    @Column(name = "email", unique = true)
    @NotEmpty(message = "Email can not be blank.")
    private String email;
    @Lob
    @Column(name = "avatar")
    private byte[] avatar;
    @Column(name = "is_activate")
    private int isActivate;
    @Column(name = "address")
    private String address;
    @Column(name = "create_at", columnDefinition = "datetime default current_timestamp", insertable = false, updatable = false)
    private LocalDateTime createAt;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @PrePersist
    @PreUpdate
    private void prepareData(){
        this.email = email == null ? null : email.toLowerCase();
        this.username = username == null ? null : username.toLowerCase();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public int getIsActivate() {
        return isActivate;
    }

    public void setIsActivate(int isActivate) {
        this.isActivate = isActivate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
