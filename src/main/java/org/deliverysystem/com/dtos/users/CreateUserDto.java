package org.deliverysystem.com.dtos.users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.deliverysystem.com.annotations.RequiredForRole;
import org.deliverysystem.com.annotations.RequiredForRoles;
import org.deliverysystem.com.enums.Role;

@Schema(description = "Дані для створення нового користувача системи")
@RequiredForRoles({
        @RequiredForRole(
                roles = { Role.EMPLOYEE },
                field = "branchId",
                message = "Для працівника необхідно вказати відділення"
        )
})
public record CreateUserDto(
        @Schema(description = "Email користувача (обов'язковий)", example = "ivan.petrenko@gmail.com")
        @NotBlank(message = "Email є обов'язковим")
        @Email(message = "Невірний формат email")
        String email,

        @Schema(description = "Ім'я", example = "Іван")
        String firstName,

        @Schema(description = "Прізвище", example = "Петренко")
        String lastName,

        @Schema(description = "По батькові", example = "Миколайович")
        String middleName,

        @Schema(description = "Номер телефону", example = "+380991234567")
        @Pattern(
                regexp = "^$|^(\\+380|0)[0-9]{9}$",
                message = "Номер телефону має бути у форматі +380XXXXXXXXX або 0XXXXXXXXX"
        )
        String phoneNumber,

        @Schema(description = "ID відділення (обов'язковий для ролі EMPLOYEE)", example = "5")
        Integer branchId,

        @Schema(description = "Роль користувача", example = "EMPLOYEE",
                allowableValues = {"EMPLOYEE", "COURIER", "DRIVER", "ADMIN", "SUPER_ADMIN"})
        @NotNull(message = "Роль є обов'язковою")
        Role role
) {}