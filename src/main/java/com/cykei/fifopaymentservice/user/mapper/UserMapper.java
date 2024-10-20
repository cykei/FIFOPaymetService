package com.cykei.fifopaymentservice.user.mapper;

import com.cykei.fifopaymentservice.user.User;
import com.cykei.fifopaymentservice.user.service.dto.UserSignUpRequest;
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
