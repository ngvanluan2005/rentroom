package com.luannv.rentroom.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @PrePersist
    @PreUpdate
    private void init(){
        this.name = this.name.toUpperCase();
    }
    @OneToMany(mappedBy = "role")
    private List<UserEntity> users;
}
