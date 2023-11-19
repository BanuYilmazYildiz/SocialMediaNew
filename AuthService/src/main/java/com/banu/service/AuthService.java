package com.banu.service;

import com.banu.dto.request.*;
import com.banu.dto.response.RegisterResponseDto;
import com.banu.exception.AuthServiceException;
import com.banu.exception.ErrorType;
import com.banu.manager.UserProfileManager;
import com.banu.mapper.AuthMapper;
import com.banu.repository.AuthServiceRepository;
import com.banu.repository.entity.Auth;
import com.banu.utility.CodeGenerator;
import com.banu.utility.JwtTokenManager;
import com.banu.utility.ServiceManager;
import com.banu.utility.enums.Status;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth, Long> {

    private final AuthServiceRepository authServiceRepository;
    private final UserProfileManager userProfileManager;
    private final JwtTokenManager jwtTokenManager;

    public AuthService(AuthServiceRepository authServiceRepository, UserProfileManager userProfileManager, JwtTokenManager jwtTokenManager) {
        super(authServiceRepository);
        this.authServiceRepository = authServiceRepository;
        this.userProfileManager = userProfileManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = AuthMapper.INSTANCE.fromRegisterRequestDto(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        save(auth);
        userProfileManager.createUser(AuthMapper.INSTANCE.createUserRequestDtofromAuth(auth));
        return AuthMapper.INSTANCE.registerResponseDtofromAuth(auth);
    }


    public String login(LoginRequestDto dto) {
        Optional<Auth> auth = authServiceRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        if (auth.isEmpty()) {
            throw new AuthServiceException(ErrorType.LOGIN_ERROR);
        }
        if (!auth.get().getStatus().equals(Status.ACTIVE)) {
            throw new AuthServiceException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
        Optional<String> token = jwtTokenManager.createToken(auth.get().getId());
        if (token.isEmpty()){
            throw new AuthServiceException(ErrorType.TOKEN_NOT_CREATED);
        }
        return token.get();
    }


    public Boolean updateStatus(UpdateStatusRequestDto dto) {
        Optional<Auth> auth = authServiceRepository.findById(dto.getId());
        if (auth.isEmpty()) {
            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
        }
        if (auth.get().getActivationCode().equals(dto.getActivationCode())) {
            auth.get().setStatus(Status.ACTIVE);
            update(auth.get());
            UserProfileActivateStatusRequestDto activateStatusRequestDto = UserProfileActivateStatusRequestDto.builder()
                    .authId(auth.get().getId())
                    .build();
            userProfileManager.activeStatus(activateStatusRequestDto);
            return true;
        } else {
            throw new AuthServiceException(ErrorType.ACTIVATION_CODE_ERROR);
        }
    }

    public Boolean updateEmailOrUsername(UpdateUsernameOrEmailRequestDto dto) {
        Optional<Auth> auth = authServiceRepository.findById(dto.getId());
        if (auth.isEmpty()){
            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setUsername(dto.getUsername());
        auth.get().setEmail(dto.getEmail());
        update(auth.get());
        return true;
    }

    public Boolean deleteAuth(Long id) {
        Optional<Auth> auth = authServiceRepository.findById(id);
        if (auth.isEmpty()){
            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setStatus(Status.DELETED);
        update(auth.get());
        DeleteUserProfileRequestDto dto = DeleteUserProfileRequestDto.builder()
                .authId(auth.get().getId())
                .build();
        userProfileManager.deleteUserProfile(dto);
        return true;
    }
}
