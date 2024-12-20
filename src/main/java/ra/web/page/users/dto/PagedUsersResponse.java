package ra.web.page.users.dto;

import ra.web.page.users.User;

import java.util.List;

public record PagedUsersResponse(List<User> users, int currentPage, long totalItems, int totalPages) {
}
