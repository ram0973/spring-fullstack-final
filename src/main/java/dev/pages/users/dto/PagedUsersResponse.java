package dev.pages.users.dto;

import dev.pages.users.User;

import java.util.List;

public record PagedUsersResponse(List<User> users, int currentPage, long totalItems, int totalPages) {
}
