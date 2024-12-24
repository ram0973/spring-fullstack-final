package dev.pages.roles;

import dev.pages.roles.dto.UserRoleCreateRequest;
import dev.pages.roles.dto.UserRoleUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRoleMapper {
    UserRoleMapper INSTANCE = Mappers.getMapper(UserRoleMapper.class);

    //@Mapping(source = "numberOfSeats", target = "seatCount")
    //@Mapping(target = "id", ignore = true)
    UserRole userRoleFromUserRoleRequest(UserRoleCreateRequest dto);

    //@Mapping(target = "id", ignore = true)
    void update(@MappingTarget UserRole userRole, UserRoleUpdateRequest dto);
}
