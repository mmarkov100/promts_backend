package com.promts.promts_test_server.promts_backend.dto.Auth.inboind;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailAndPasswordDTO {

    @NotBlank
    @Email(                   // базовая проверка RFC 5322
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Некорректный формат e-mail")
    private String email;
    @NotBlank
    @Size(min = 6, max = 64,
            message = "Пароль должен быть от 6 до 64 символов")
    @Pattern(
            regexp = "^[\\x21-\\x7E]+$",
            message = "Пароль может содержать только латиницу, цифры и ASCII-знаки")
    private String password;

}
