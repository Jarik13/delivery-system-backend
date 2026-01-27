package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Поштомат")
public record PostomatDto(
        @Schema(description = "ID поштомату", example = "1")
        Integer id,

        @NotBlank(message = "Вкажіть назву поштомату")
        @Size(max = 100, message = "Назва не може бути довшою за 100 символів")
        @Schema(description = "Назва", example = "Поштомат №2045")
        String name,

        @NotBlank(message = "Вкажіть адресу (вулицю та номер)")
        @Size(max = 255, message = "Адреса занадто довга")
        @Schema(description = "Адреса", example = "в магазині АТБ")
        String address,

        @NotNull(message = "Оберіть населений пункт")
        @Schema(description = "ID міста (для форми)", example = "5")
        Integer cityId,

        @Schema(description = "Назва міста (для відображення)", example = "Львів", accessMode = Schema.AccessMode.READ_ONLY)
        String cityName,

        @Schema(description = "Технічний код", example = "P-2045")
        String code,

        @NotNull(message = "Вкажіть кількість комірок")
        @PositiveOrZero(message = "Кількість комірок не може бути від'ємною")
        @Schema(description = "Кількість комірок")
        Integer cellsCount,

        @Schema(description = "Чи активний")
        Boolean isActive
) {}