package dev.pages.users;

import dev.pages.users.dto.PagedUsersResponse;
import dev.pages.users.dto.UserCreateRequest;
import dev.pages.users.dto.UserUpdateRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

public interface IUserService {
    Optional<PagedUsersResponse> findAllPaged(int page, int size, String[] sort);

    Optional<User> findById(int id);

    Optional<User> findUserByEmailIgnoreCase(String email);

    @Transactional
    User createUser(@NotNull UserCreateRequest dto) throws IOException;

    @Transactional
    User updateUser(int id, @NotNull UserUpdateRequest dto) throws IOException;

    void deleteUser(int id);
}
