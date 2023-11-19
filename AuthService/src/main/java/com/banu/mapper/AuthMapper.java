package com.banu.mapper;

import com.banu.dto.request.CreateUserProfileRequestDto;
import com.banu.dto.request.RegisterRequestDto;
import com.banu.dto.response.RegisterResponseDto;
import com.banu.repository.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    Auth fromRegisterRequestDto(final RegisterRequestDto dto);

    RegisterResponseDto registerResponseDtofromAuth(final Auth auth);

    @Mapping(source = "id",target = "authId")
    CreateUserProfileRequestDto createUserRequestDtofromAuth(final Auth auth);

}
