package com.logistics.sdg.mapper;

import com.logistics.sdg.dto.packages.CreatePackageRequest;
import com.logistics.sdg.dto.packages.PackageResponse;
import com.logistics.sdg.model.Package;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PackageMapper {
    Package toEntity(CreatePackageRequest request);
    PackageResponse toResponse(Package colis);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePackagesFromRequest(CreatePackageRequest request, @MappingTarget Package colis);
}
