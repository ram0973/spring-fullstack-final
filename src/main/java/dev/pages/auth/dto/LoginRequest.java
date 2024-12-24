package dev.pages.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
    @NotNull @NotBlank(message = "Email cannot be empty") @Email String email,
    @NotNull @NotBlank(message = "Password cannot be empty") String password) {
}
