package com.luannv.rentroom.mapper;

import com.luannv.rentroom.dto.request.UserUpdateRequest;
import com.luannv.rentroom.dto.response.UserUpdateResponse;
import com.luannv.rentroom.entity.UserEntity;
import com.luannv.rentroom.utils.StringHandleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.luannv.rentroom.constants.UrlConstants.AVATAR_FORMAT_URL;
import static com.luannv.rentroom.constants.UrlConstants.DEFAULT_AVATAR;

@Component
public class UserUpdateMapper implements GenericMapper<UserEntity, UserUpdateRequest, UserUpdateResponse> {
	private final PasswordEncoder passwordEncoder;
	private final StringHandleUtils stringHandleUtils;

	@Autowired
	public UserUpdateMapper(PasswordEncoder passwordEncoder, StringHandleUtils stringHandleUtils) {
		this.passwordEncoder = passwordEncoder;
		this.stringHandleUtils = stringHandleUtils;
	}

	@Override
	public UserEntity toEntity(UserUpdateRequest requestDTO) {
		if (requestDTO == null)
			return null;

		UserEntity.UserEntityBuilder entityBuilder = UserEntity.builder();

		if (requestDTO.getFirstName() != null && requestDTO.getLastName() != null) {
			entityBuilder.fullName(
							this.stringHandleUtils.toCapitalizeString(requestDTO.getFirstName() + " " + requestDTO.getLastName()));
		}
		if (requestDTO.getPassword() != null) {
			entityBuilder.password(passwordEncoder.encode(requestDTO.getPassword()));
		}
		if (requestDTO.getAddress() != null) {
			entityBuilder.address(requestDTO.getAddress());
		}
		if (requestDTO.getUsername() != null) {
			entityBuilder.username(requestDTO.getUsername());
		}
		if (requestDTO.getEmail() != null) {
			entityBuilder.email(requestDTO.getEmail());
		}
		// avatar manually config in service :>
		return entityBuilder.build();
	}

	@Override
	public UserUpdateResponse toResponseDTO(UserEntity userEntity) {
		if (userEntity == null)
			return null;

		String avatarLink = (userEntity.getAvatar() != null)
						? String.format(AVATAR_FORMAT_URL, userEntity.getUsername())
						: DEFAULT_AVATAR;

		UserUpdateResponse.UserUpdateResponseBuilder responseBuilder = UserUpdateResponse.builder();

		if (userEntity.getUsername() != null) {
			responseBuilder.username(userEntity.getUsername());
		}
		if (userEntity.getFullName() != null) {
			responseBuilder.fullName(userEntity.getFullName());
		}
		if (userEntity.getAddress() != null) {
			responseBuilder.address(userEntity.getAddress());
		}
		if (userEntity.getEmail() != null) {
			responseBuilder.email(userEntity.getEmail());
		}
		if (userEntity.getRole() != null && userEntity.getRole().getName() != null) {
			responseBuilder.roleName(userEntity.getRole().getName());
		}

		responseBuilder.avatar(avatarLink);

		return responseBuilder.build();
	}
}
