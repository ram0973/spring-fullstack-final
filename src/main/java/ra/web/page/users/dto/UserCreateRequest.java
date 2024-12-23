package ra.web.page.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import ra.web.page.roles.UserRole;
import ra.web.page.users.User;

import java.util.List;

public record UserCreateRequest(
    MultipartFile avatar,
    @NotBlank @Email String email,
    boolean enabled,
    String firstName,
    String lastName,
    @NotBlank
    @Size(min = 6, message = "Password must have 6 symbols or more")
    @Size(max = 64, message = "Password length more than 64 symbols")
    String password,
    //@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    List<User.Role> roles
) {
}
