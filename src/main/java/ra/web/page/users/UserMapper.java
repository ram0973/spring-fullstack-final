package ra.web.page.users;

import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import ra.web.common.mappers.JsonNullableMapper;
import ra.web.common.mappers.ReferenceMapper;
import ra.web.page.roles.UserRole;
import ra.web.page.users.dto.*;
import ra.web.page.users.dto.UsersResponse;

import java.util.List;
import java.util.Set;


@Mapper(
    uses = {JsonNullableMapper.class, ReferenceMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "avatar", ignore = true)//, qualifiedByName = "mapRolesStringsToEntities")
    public abstract User map(UserCreateRequest dto);

    public abstract UserResponse map(User user);

    @Mapping(target = "avatar", ignore = true)
    public abstract void update(@MappingTarget User user, UserUpdateRequest dto);

    //@Named("mapRolesStringsToEntities")
    //public List<UserRole> mapRolesStringsToEntities(List<User.Role> roles) {
    //    return null;
    //}
}
