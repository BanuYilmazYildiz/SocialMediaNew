package com.banu.service;

import com.banu.dto.request.*;
import com.banu.exception.ErrorType;
import com.banu.exception.UserManagerException;
import com.banu.manager.AuthManager;
import com.banu.mapper.UserProfileMapper;
import com.banu.repository.UserProfileRepository;
import com.banu.repository.entity.UserProfile;
import com.banu.utility.JwtTokenManager;
import com.banu.utility.ServiceManager;
import com.banu.utility.enums.Status;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService extends ServiceManager<UserProfile, Long> {

    private final UserProfileRepository userProfileRepository;
    private final JwtTokenManager jwtTokenManager;
    private final AuthManager authManager;

    public UserProfileService(UserProfileRepository userProfileRepository, JwtTokenManager jwtTokenManager, AuthManager authManager) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.authManager = authManager;
    }

    public Boolean createUser(CreateUserProfileRequestDto dto) {
        try {
            save(UserProfileMapper.INSTANCE.fromCreateUserProfileDto(dto));
            return true;
        }catch (Exception e){
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }

    }

    public Boolean activeStatus(UserProfileActivateStatusRequestDto dto) {
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(dto.getAuthId());
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(Status.ACTIVE);
        update(userProfile.get());
        return true;
    }

    public Boolean updateUserProfile(UpdateUserProfileRequestDto dto) {
        Optional<Long> authId = jwtTokenManager.getIdFromToken(dto.getToken());
        if (authId.isEmpty()){
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        if (!userProfile.get().getEmail().equals(dto.getEmail()) || !userProfile.get().getUsername().equals(dto.getUsername())){
            userProfile.get().setUsername(dto.getUsername());
            userProfile.get().setEmail(dto.getEmail());
            UpdateUsernameOrEmailRequestDto updateUsernameOrEmailRequestDto = UpdateUsernameOrEmailRequestDto.builder()
                    .email(userProfile.get().getEmail())
                    .username(userProfile.get().getUsername())
                    .id(userProfile.get().getAuthId())
                    .build();
            authManager.updateEmailOrUsername(updateUsernameOrEmailRequestDto);
        }
        userProfile.get().setPhone(dto.getPhone());
        userProfile.get().setAvatarUrl(dto.getAvatarUrl());
        userProfile.get().setPhone(dto.getPhone());
        userProfile.get().setAbout(dto.getAbout());
        userProfile.get().setAddress(dto.getAddress());
        update(userProfile.get());
        return true;
    }

    public Boolean deleteUserProfile(DeleteUserProfileRequestDto dto) {
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(dto.getAuthId());
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(Status.DELETED);
        update(userProfile.get());
        return true;
    }
}
