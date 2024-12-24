package dev.pages.users;

import dev.pages.users.dto.UserCreateRequest;
import dev.pages.users.dto.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //@Mapping(source = "numberOfSeats", target = "seatCount")
    //@Mapping(target = "id", ignore = true)
    //@Mapping(target = "roles", ignore = true)
    //@Mapping(target = "avatar", ignore = true)
    User userFromUserRequest(UserCreateRequest dto);

    //@Mapping(target = "avatar", ignore = true)
    //@Mapping(target = "password", ignore = true)
    void update(@MappingTarget User user, UserUpdateRequest dto);
}
