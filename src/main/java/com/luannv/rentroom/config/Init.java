package com.luannv.rentroom.config;

import com.luannv.rentroom.entity.Role;
import com.luannv.rentroom.entity.UserEntity;
import com.luannv.rentroom.enums.RoleEnums;
import com.luannv.rentroom.repository.RoleRepository;
import com.luannv.rentroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class Init implements CommandLineRunner {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;

	private final String username = "admin";
	private final String password = "123";
	private final List<RoleEnums> roleEnums = Arrays.asList(RoleEnums.values());
	@Autowired
	public Init(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
	}
	@Override
	public void run(String... args) throws Exception {
		if (!this.roleRepository.existsByName(RoleEnums.ADMIN.name())) {
			roleEnums.forEach(roleName -> {
				this.roleRepository.save(Role.builder().name(roleName.name()).build());
			});
		}
		Optional<Role> adminRole = this.roleRepository.findByName(RoleEnums.ADMIN.name());
		if (!this.userRepository.existsByUsername(username)) {
			this.userRepository.save(UserEntity.builder()
							.username(username)
							.password(passwordEncoder.encode(password))
							.isActivate(1)
							.role(adminRole.get())
							.build());
		}
	}
}
