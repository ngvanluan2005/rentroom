package com.luannv.rentroom.service;

import com.luannv.rentroom.dto.request.IntrospectRequest;
import com.luannv.rentroom.dto.request.LogoutRequest;
import com.luannv.rentroom.dto.request.UserLoginRequestDTO;
import com.luannv.rentroom.dto.request.UserRegisterRequestDTO;
import com.luannv.rentroom.dto.response.AuthenticationResponse;
import com.luannv.rentroom.dto.response.IntrospectResponse;
import com.luannv.rentroom.dto.response.UserResponseDTO;
import com.luannv.rentroom.entity.InvalidatedToken;
import com.luannv.rentroom.entity.UserEntity;
import com.luannv.rentroom.exception.ErrorCode;
import com.luannv.rentroom.exception.SingleErrorException;
import com.luannv.rentroom.mapper.UserMapper;
import com.luannv.rentroom.repository.InvalidatedTokenRepository;
import com.luannv.rentroom.repository.RoleRepository;
import com.luannv.rentroom.repository.UserRepository;
import com.luannv.rentroom.utils.SecurityUtils;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.luannv.rentroom.constants.UrlConstants.API_USER;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityUtils securityUtils;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    @NonFinal
    @Value("${security.jwt.signerKey}")
    protected String KEY_SIGNER;
    @Autowired
    public AuthService(UserRepository userRepository, UserMapper userMapper, SecurityUtils securityUtils, RoleRepository roleRepository, PasswordEncoder passwordEncoder, InvalidatedTokenRepository invalidatedTokenRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.securityUtils = securityUtils;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }
    public Map<?, ?> validateUserRequest(BindingResult bindingResult, UserRegisterRequestDTO userRequestDTO) {
        Map<String, String> errorsResponse = new HashMap<>();
        bindingResult.getFieldErrors()
                .forEach(err -> errorsResponse.put(err.getField(), ErrorCode.valueOf(err.getDefaultMessage()).getMessages()));
        // key: field, value: defaultMessages->ErrorCode CONST
        // why do not use <String, List<String>>, if has error, and map has key error, then not catch error :)
        if (errorsResponse.get("username") == null && this.userRepository.existsByUsername(userRequestDTO.getUsername()))
            errorsResponse.put("username", ErrorCode.USERNAME_EXISTED.getMessages());
        if (errorsResponse.get("email") == null && this.userRepository.existsByEmail(userRequestDTO.getEmail()))
            errorsResponse.put("email", ErrorCode.EMAIL_EXISTED.getMessages());

//        errorsResponse.forEach((key, value) -> System.out.println(key + " " + value));
        return errorsResponse;
    }
    public UserResponseDTO addUser(UserRegisterRequestDTO userRequestDTO, MultipartFile multipartFile) {
        UserEntity userEntity = this.userMapper.toEntity(userRequestDTO);
        if (this.securityUtils.getCurrentRole() != null && this.securityUtils.getCurrentRole().equalsIgnoreCase("ROLE_ADMIN")) {
            userEntity.setRole(this.roleRepository.findById(userRequestDTO.getRoleId()).get());
        } else {
            userEntity.setRole(this.roleRepository.findById(3).get());
        }
        if (multipartFile != null && !multipartFile.isEmpty()) {
            try {
                UserResponseDTO userResponseDTO = this.userMapper.toResponseDTO(userEntity);
                userResponseDTO.setAvatar(API_USER+"/"+userEntity.getUsername()+"/avatar");
                userEntity.setAvatar(multipartFile.getBytes());
                UserEntity saved = this.userRepository.save(userEntity);
                userResponseDTO.setUserId(saved.getId());
                return userResponseDTO;
            } catch (IOException e) {
                throw new RuntimeException("ErrorCode: ", e);
            }
        }
        UserEntity saved = this.userRepository.save(userEntity);
        return this.userMapper.toResponseDTO(saved);
    }
    public AuthenticationResponse loginUserValidate(UserLoginRequestDTO userLoginRequestDTO) {
        Optional<UserEntity> optionalUserEntity = this.userRepository.findByUsername(userLoginRequestDTO.getUsername());
        if (optionalUserEntity.isEmpty())
            return null;
        UserEntity userEntity = optionalUserEntity.get();
        if (!passwordEncoder.matches(userLoginRequestDTO.getPassword(), userEntity.getPassword()))
            return null;
        String token = generateToken(userLoginRequestDTO.getUsername(), Arrays.asList(userEntity.getRole().getName()));
        return AuthenticationResponse.builder()
                .token(token)
                .authenicated(true)
                .build();
    }

    public String generateToken(String username, List<String> roles) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("luannv.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("roles", roles)
                .build();
        JWSObject jwsObject = new JWSObject(jwsHeader, jwtClaimsSet.toPayload());
        try {
            jwsObject.sign(new MACSigner(KEY_SIGNER.getBytes(StandardCharsets.UTF_8)));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return "";
    }
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) {
        String token = introspectRequest.getToken();
        boolean isValid = true;
        try {
            verifyToken(token);
        } catch (JOSEException | ParseException | SingleErrorException e ) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }
    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        try {
            JWSVerifier verifier = new MACVerifier(KEY_SIGNER.getBytes(StandardCharsets.UTF_8));
            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean verified = signedJWT.verify(verifier);
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (!(verified && expiryTime.after(new Date())))
                throw new SingleErrorException(ErrorCode.UNAUTHENTICATED);
            if (this.invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
                throw new SingleErrorException(ErrorCode.UNAUTHENTICATED);
            return signedJWT;
        } catch (Exception e) {
            throw new SingleErrorException(ErrorCode.UNAUTHENTICATED);
        }
    }
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken());
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
        this.invalidatedTokenRepository.save(invalidatedToken);
    }
}
