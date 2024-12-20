package ra.web.page.users;

import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import ra.web.common.mappers.JsonNullableMapper;
import ra.web.common.mappers.ReferenceMapper;
import ra.web.page.users.dto.PagedUsersResponse;
import ra.web.page.users.dto.UserCreateRequest;
import ra.web.page.users.dto.UserUpdateRequest;


@Mapper(
    uses = {JsonNullableMapper.class, ReferenceMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    public abstract User map(PagedUsersResponse pagedUsersResponse);

    @Mapping(source = "avatar", target = "avatar", ignore = true)
    public abstract User map(UserCreateRequest dto);

    @Mapping(source = "avatar", target = "avatar", ignore = true)
    public abstract void update(@MappingTarget User user, UserUpdateRequest dto);
}
