package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.deliverysystem.com.annotations.DifferentBranches;

@DifferentBranches
@Schema(description = "Шаблон магістрального маршруту")
public record RouteDto(
        @Schema(description = "Унікальний ідентифікатор маршруту", example = "25")
        Integer id,

        @NotNull(message = "Оберіть відділення відправлення")
        @Schema(description = "ID відділення відправлення", example = "1")
        Integer originBranchId,

        @Schema(description = "Назва відділення відправлення", example = "Відділення №1", accessMode = Schema.AccessMode.READ_ONLY)
        String originBranchName,

        @Schema(description = "Назва міста відправлення", example = "Київ", accessMode = Schema.AccessMode.READ_ONLY)
        String originCityName,

        @NotNull(message = "Оберіть відділення призначення")
        @Schema(description = "ID відділення призначення", example = "2")
        Integer destinationBranchId,

        @Schema(description = "Назва відділення призначення", example = "Відділення №5", accessMode = Schema.AccessMode.READ_ONLY)
        String destinationBranchName,

        @Schema(description = "Назва міста призначення", example = "Львів", accessMode = Schema.AccessMode.READ_ONLY)
        String destinationCityName,

        @Schema(description = "Чи потрібне сортування на маршруті", example = "false")
        Boolean needSorting
) {}