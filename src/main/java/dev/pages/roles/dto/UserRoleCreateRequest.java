package dev.pages.roles.dto;

import dev.pages.users.User;

public record UserRoleCreateRequest(
    User.Role role
) {
}
