package ra.web.page.users.dto;

import ra.web.page.roles.UserRole;
import ra.web.page.users.User;

import java.util.List;
import java.util.Set;

public record UserResponse(
    String avatar,
    String email,
    boolean enabled,
    String firstName,
    String lastName,
    String password,
    List<UserRole> roles)
{}
