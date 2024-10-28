package com.fifo.userservice.mapper;

import com.fifo.userservice.entity.User;
import com.fifo.userservice.service.dto.UserSignUpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "email", source = "email")
    User mapToEntity(UserSignUpRequest request);
}
