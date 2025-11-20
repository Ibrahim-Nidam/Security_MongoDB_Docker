package com.logistics.sdg.controller;

import com.logistics.sdg.dto.packages.CreatePackageRequest;
import com.logistics.sdg.dto.packages.PackageResponse;
import com.logistics.sdg.model.enums.PackageStatus;
import com.logistics.sdg.model.enums.Specialty;
import com.logistics.sdg.service.PackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/packages")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPackageController {
    private final PackageService packageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PackageResponse createPackage(@Valid @RequestBody CreatePackageRequest request){
        return packageService.createPackage(request);
    }

    @GetMapping
    public Page<PackageResponse> getAllPackages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false)Specialty type,
            @RequestParam(required = false)PackageStatus status,
            @RequestParam(required = false) String addressContains
            ){
        return packageService.findAllPaginated(page, size, type, status, addressContains);
    }

    @PatchMapping("/{colisId}/assign/{transporterId}")
    public PackageResponse assignPackage(@PathVariable String colisId, @PathVariable String transporterId){
        return packageService.assignPackage(colisId, transporterId);
    }
}
