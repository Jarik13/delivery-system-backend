package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Шаблон магістрального маршруту")
public record RouteDto(
        @Schema(description = "Унікальний ідентифікатор маршруту", example = "25")
        Integer id,

        @NotNull
        @Schema(description = "ID відділення відправлення", example = "1")
        Integer originBranchId,

        @Schema(description = "Назва відділення відправлення", example = "Відділення №1 (Київ)", accessMode = Schema.AccessMode.READ_ONLY)
        String originBranchName,

        @NotNull
        @Schema(description = "ID відділення призначення", example = "2")
        Integer destinationBranchId,

        @Schema(description = "Назва відділення призначення", example = "Відділення №5 (Львів)", accessMode = Schema.AccessMode.READ_ONLY)
        String destinationBranchName,

        @Schema(description = "Чи потрібне сортування на маршруті", example = "false")
        Boolean isNeedSorting
) {}