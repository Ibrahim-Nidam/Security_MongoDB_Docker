package com.logistics.sdg.dto.packages;

import com.logistics.sdg.model.enums.PackageStatus;
import com.logistics.sdg.model.enums.Specialty;

public record PackageResponse(
        String id,
        Specialty type,
        double weight,
        String destinationAddress,
        PackageStatus status,
        String manutation,
        Double temperatureMin,
        Double temperatureMax,
        String transporterId
) {
}
