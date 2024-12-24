package dev.pages.users;

import dev.common.exceptions.EntityAlreadyExistsException;
import dev.common.exceptions.ForbiddenOperationException;
import dev.common.exceptions.NoSuchEntityException;
import dev.pages.MultiPartFileUtils;
import dev.pages.PagedEntityUtils;
import dev.pages.roles.UserRole;
import dev.pages.roles.UserRoleRepository;
import dev.pages.users.dto.PagedUsersResponse;
import dev.pages.users.dto.UserCreateRequest;
import dev.pages.users.dto.UserUpdateRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Override
    public Optional<PagedUsersResponse> findAllPaged(int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(PagedEntityUtils.getSortOrders(sort)));
        Page<User> pagedUsers = userRepository.findAll(pageable);
        List<User> Users = pagedUsers.getContent();
        if (Users.isEmpty()) {
            return Optional.empty();
        } else {
            PagedUsersResponse PagedUsersResponseDto = new PagedUsersResponse(Users, pagedUsers.getNumber(),
                pagedUsers.getTotalElements(), pagedUsers.getTotalPages());
            return Optional.of(PagedUsersResponseDto);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findUserByEmailIgnoreCase(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    @Transactional
    public User createUser(@NotNull UserCreateRequest dto) throws IOException {
        Optional<User> optionalUser = findUserByEmailIgnoreCase(dto.email().strip());
        if (optionalUser.isPresent()) {
            throw new EntityAlreadyExistsException("Email already in use");
        }
        User user = UserMapper.INSTANCE.userFromUserRequest(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<UserRole> allRoles = userRoleRepository.findAll();
        if (dto.roles() != null && !dto.roles().isEmpty()) {
            for (User.Role dtoRole : dto.roles()) {
                UserRole userRole = userRoleRepository.findByRole(dtoRole).orElse(null);
                if (userRole == null || !allRoles.contains(userRole)) {
                    continue;
                }
                userRole.getUsers().add(user);
                user.addRole(userRole);
            }
        }
        if (dto.avatar() != null && dto.avatar().getOriginalFilename() != null) {
            String newImagePath = MultiPartFileUtils.saveMultiPartImage(dto.avatar());
            user.setAvatar(newImagePath);
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(int id, @NotNull UserUpdateRequest dto) throws IOException {
        User user = userRepository.findById(id).orElseThrow(
            () -> new NoSuchEntityException("No such User with id: " + id));
        UserMapper.INSTANCE.update(user, dto);
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        List<UserRole> allRoles = userRoleRepository.findAll();
        if (dto.roles() != null && !dto.roles().isEmpty()) {
            user.setRoles(new HashSet<>());
            for (User.Role dtoRole : dto.roles()) {
                UserRole userRole = userRoleRepository.findByRole(dtoRole).orElse(null);
                if (userRole == null || !allRoles.contains(userRole)) {
                    continue;
                }
                user.addRole(userRole);
            }
        }
        if (dto.avatar() != null && dto.avatar().getOriginalFilename() != null) {
            String newImagePath = MultiPartFileUtils.saveMultiPartImage(dto.avatar());
            user.setAvatar(newImagePath);
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(int id) {
        User user = findById(id).orElseThrow(
            () -> new NoSuchEntityException("No such User with id: " + id));
        if (user.getEmail().equals(adminEmail)) {
            throw new ForbiddenOperationException("You cannot delete admin account");
        }
        userRepository.deleteById(id);
    }
}
