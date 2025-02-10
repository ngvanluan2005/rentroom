package com.luannv.rentroom.entity;

import com.luannv.rentroom.exception.Error;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

import static com.luannv.rentroom.exception.Error.ROLE_INVALID;

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
