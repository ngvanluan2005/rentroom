package com.luannv.rentroom.service;

import com.luannv.rentroom.entity.UserEntity;
import com.luannv.rentroom.exception.ErrorCode;
import com.luannv.rentroom.exception.ListErrorException;
import com.luannv.rentroom.exception.SingleErrorException;
import com.luannv.rentroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new SingleErrorException(ErrorCode.USERNAME_NOT_EXISTED));
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole().getName()));
        return new User(userEntity.getUsername(), userEntity.getPassword(), authorities);
    }
}
