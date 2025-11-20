package com.logistics.sdg.controller;

import com.logistics.sdg.dto.packages.PackageResponse;
import com.logistics.sdg.model.User;
import com.logistics.sdg.model.enums.PackageStatus;
import com.logistics.sdg.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transporter/package")
@PreAuthorize("hasRole('TRANSPORTOR')")
public class TransporterPackageController {
    private final PackageService packageService;

    @GetMapping("/my-packages")
    public Page<PackageResponse> getMyPackages(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "5") int size){
        User transporter = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return packageService.findMyPackages(transporter.getId(), page, size);
    }

    @PatchMapping("/{colisId}/deliver")
    public PackageResponse markAsDelivered(@PathVariable String colisId){
        return packageService.updateStatus(colisId, PackageStatus.DELIVERED);
    }
}
