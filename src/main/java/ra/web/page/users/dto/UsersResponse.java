package ra.web.page.users.dto;

import java.util.List;

public record UsersResponse(
    List<UserResponse> users,
    int currentPage,
    long totalItems,
    int totalPages) {
}
