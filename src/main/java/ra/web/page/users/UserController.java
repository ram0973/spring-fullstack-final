package ra.web.page.users;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.web.common.exceptions.NoSuchEntityException;
import ra.web.page.users.dto.PagedUsersResponse;
import ra.web.page.users.dto.UserCreateRequest;
import ra.web.page.users.dto.UserUpdateRequest;

import java.io.IOException;
import java.util.Collections;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    @NonNull
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')") // TODO: check everywhere for opportunity to use User.Role.ADMIN
    public PagedUsersResponse getUsers(
        @RequestParam(required = false) String email,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id,desc") String[] sort
    ) {
        // TODO: check if really need Optional here
        PagedUsersResponse pagedUsersResponse = userService.findAllPaged(page, size, sort).orElse(
            new PagedUsersResponse(Collections.emptyList(), 0, 0, 0)
        );
        return pagedUsersResponse;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User getUserById(@PathVariable("id") int id) {
        User user = userService.findById(id).orElseThrow(
            () -> new NoSuchEntityException("No such user with id: " + id));
        return user;
    }

    // this is for admins
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    // with @RequestParam instead of @ModelAttribute we get Content type not supported!
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @ModelAttribute UserCreateRequest dto) throws IOException {
        User user = userService.createUser(dto);
        return user;
    }

    @PatchMapping(path = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public User updateUser(
        @PathVariable("id") int id, @Valid @ModelAttribute UserUpdateRequest dto) throws IOException {
        User user = userService.updateUser(id, dto);
        return user;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "Successfully deleted user with id: " + id;
    }
}
