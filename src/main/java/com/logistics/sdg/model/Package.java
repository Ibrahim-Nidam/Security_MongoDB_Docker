package com.logistics.sdg.model;

import com.logistics.sdg.model.enums.PackageStatus;
import com.logistics.sdg.model.enums.Specialty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "packages")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Package {
    @Id
    private String id;

    private Specialty type;
    private double weight;
    private String destinationAddress;
    private PackageStatus status = PackageStatus.WAITING;
    private String manutation;
    private Double temperatureMin;
    private Double temperatureMax;
    private String transportorId;
}
