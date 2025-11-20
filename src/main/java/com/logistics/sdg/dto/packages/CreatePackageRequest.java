package com.logistics.sdg.dto.packages;

import com.logistics.sdg.model.enums.Specialty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreatePackageRequest(
        @NotNull Specialty type,
        @Positive Double weight,
        @NotBlank String destinationAddress,
        String manutation,
        Double temperatureMin,
        Double temperatureMax) {
}
