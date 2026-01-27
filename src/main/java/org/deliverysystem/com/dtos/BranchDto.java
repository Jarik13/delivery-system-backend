package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Дані про відділення доставки")
public record BranchDto(
        @Schema(description = "Унікальний ідентифікатор відділення", example = "1")
        Integer id,

        @NotBlank(message = "Назва відділення є обов'язковою")
        @Size(min = 2, max = 100, message = "Назва повинна містити від 2 до 100 символів")
        @Schema(description = "Назва відділення", example = "Відділення №1")
        String name,

        @NotBlank(message = "Адреса є обов'язковою")
        @Size(max = 255, message = "Адреса занадто довга (макс. 255 символів)")
        @Schema(description = "Адреса (вулиця, номер будинку)", example = "вул. Шевченка, 10")
        String address,

        @NotNull(message = "Будь ласка, оберіть місто")
        @Schema(description = "ID населеного пункту", example = "5")
        Integer cityId,

        @Schema(description = "Назва міста", example = "Київ", accessMode = Schema.AccessMode.READ_ONLY)
        String cityName,

        @NotNull(message = "Виберіть тип відділення (напр. сортувальне або вантажне)")
        @Schema(description = "ID типу відділення", example = "2")
        Integer branchTypeId,

        @Schema(description = "Назва типу відділення", example = "Вантажне", accessMode = Schema.AccessMode.READ_ONLY)
        String branchTypeName
) {}