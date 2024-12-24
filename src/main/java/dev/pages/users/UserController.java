package dev.pages.users;

import dev.common.exceptions.NoSuchEntityException;
import dev.pages.users.dto.PagedUsersResponse;
import dev.pages.users.dto.UserCreateRequest;
import dev.pages.users.dto.UserUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    @NonNull private final IUserService userService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')") // TODO: check everywhere for opportunity to use User.Role.ADMIN
    public ResponseEntity<PagedUsersResponse> getUsers(
        @RequestParam(required = false) String email,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id,desc") String[] sort
    ) {
        // TODO: check if really need Optional here
        PagedUsersResponse pagedUsersResponse = userService.findAllPaged(page, size, sort).orElse(
            new PagedUsersResponse(Collections.emptyList(), 0, 0, 0)
        );
        return ResponseEntity.ok(pagedUsersResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        User user = userService.findById(id).orElseThrow(
            () -> new NoSuchEntityException("No such user with id: " + id));
        return ResponseEntity.ok(user);
    }

    public record JsonRole(String role) {

    }

    // this is for admin
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    // with @RequestParam instead of @ModelAttribute we get Content type not supported!
    public ResponseEntity<User> createUser(@Valid @ModelAttribute UserCreateRequest dto) throws IOException  {
        // check exceptions?
        User user = userService.createUser(dto);
        return ResponseEntity.ok(user);
    }

    @PatchMapping(path ="{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(
        @PathVariable("id") int id, @Valid @ModelAttribute UserUpdateRequest dto) throws IOException {
        User user = userService.updateUser(id, dto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Successfully deleted user with id: " + id);
    }
}
