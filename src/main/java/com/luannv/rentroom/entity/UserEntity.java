package com.luannv.rentroom.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "username")
    private String username;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "email")
    private String email;
    @Lob
    @Column(name = "avatar", columnDefinition = "LONGBLOB")
    private byte[] avatar;
    @Column(name = "is_activate")
    private int isActivate = 0;
    @Column(name = "address")
    private String address;
    @Column(name = "create_at", columnDefinition = "datetime default current_timestamp", insertable = false, updatable = false)
    private LocalDateTime createAt;
    @Column(name = "password")
    private String password;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    @Enumerated(EnumType.STRING)
    private Role role;

    @PrePersist
    @PreUpdate
    private void prepareData() {
        this.email = email == null ? null : email.toLowerCase();
        this.username = username == null ? null : username.toLowerCase();
        this.createAt = LocalDateTime.now();
    }
}
