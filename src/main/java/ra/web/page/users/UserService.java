package ra.web.page.users;

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
import ra.web.common.exceptions.EntityAlreadyExistsException;
import ra.web.common.exceptions.ForbiddenOperationException;
import ra.web.common.exceptions.NoSuchEntityException;
import ra.web.page.roles.UserRole;
import ra.web.page.roles.UserRoleRepository;
import ra.web.page.users.dto.PagedUsersResponse;
import ra.web.page.users.dto.UserCreateRequest;
import ra.web.page.users.dto.UserUpdateRequest;
import ra.web.common.util.MultiPartFileUtils;
import ra.web.common.util.PagedEntityUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;

    @Value("${app.admin.email}")
    private String adminEmail;

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

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByEmailIgnoreCase(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Transactional
    public User createUser(@NotNull UserCreateRequest dto) throws IOException {
        Optional<User> optionalUser = findUserByEmailIgnoreCase(dto.email().strip());
        if (optionalUser.isPresent()) {
            throw new EntityAlreadyExistsException("Email already in use");
        }
        User user = userMapper.map(dto);
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

    @Transactional
    public User updateUser(int id, @NotNull UserUpdateRequest dto) throws IOException {
        User user = userRepository.findById(id).orElseThrow(
            () -> new NoSuchEntityException("No such User with id: " + id));
        userMapper.update(user, dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    public void deleteUser(int id) {
        User user = findById(id).orElseThrow(
            () -> new NoSuchEntityException("No such User with id: " + id));
        if (user.getEmail().equals(adminEmail)) {
            throw new ForbiddenOperationException("You cannot delete admin account");
        }
        userRepository.deleteById(id);
    }
}
