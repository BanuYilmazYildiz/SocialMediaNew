package com.banu.mapper;

import com.banu.dto.request.CreateUserProfileRequestDto;
import com.banu.repository.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserProfileMapper {

    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    UserProfile fromCreateUserProfileDto(final CreateUserProfileRequestDto dto);
}
