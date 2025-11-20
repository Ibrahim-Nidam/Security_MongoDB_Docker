package com.logistics.sdg.mapper;

import com.logistics.sdg.dto.transporter.CreateTransporterRequest;
import com.logistics.sdg.dto.transporter.TransporterResponse;
import com.logistics.sdg.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransporterMapper {
    @Mapping(target = "role", constant = "TRANSPORTOR")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "status", constant = "AVAILABLE")
    @Mapping(target = "password", ignore = true)
    User toEntity(CreateTransporterRequest request);

    TransporterResponse toResponse(User user);
}
