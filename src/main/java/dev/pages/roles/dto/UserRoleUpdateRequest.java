package dev.pages.roles.dto;

import dev.pages.users.User;

public record UserRoleUpdateRequest(
    User.Role role
) {
}
