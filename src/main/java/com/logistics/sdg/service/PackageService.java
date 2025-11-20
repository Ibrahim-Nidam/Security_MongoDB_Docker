package com.logistics.sdg.service;

import com.logistics.sdg.dto.packages.CreatePackageRequest;
import com.logistics.sdg.dto.packages.PackageResponse;
import com.logistics.sdg.mapper.PackageMapper;
import com.logistics.sdg.model.Package;
import com.logistics.sdg.model.User;
import com.logistics.sdg.model.enums.PackageStatus;
import com.logistics.sdg.model.enums.Role;
import com.logistics.sdg.model.enums.Specialty;
import com.logistics.sdg.model.enums.TransporterStatus;
import com.logistics.sdg.repository.PackageRepository;
import com.logistics.sdg.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {
    private final PackageRepository packageRepository;
    private final PackageMapper packageMapper;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public PackageResponse createPackage(CreatePackageRequest request){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Package colis = packageMapper.toEntity(request);
        colis.setStatus(PackageStatus.WAITING);

        Package saved = packageRepository.save(colis);
        return packageMapper.toResponse(saved);
    }

    public List<PackageResponse> getAllPackages(){
        return packageRepository.findAll().stream()
                .map(packageMapper::toResponse)
                .toList();
    }

    public PackageResponse assignPackage(String packageId, String transporterId){
        Package colis = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Package Not found!"));

        User transporter = userRepository.findById(transporterId)
                .orElseThrow(() -> new RuntimeException("Transporter Not Found"));

        if(colis.getType() != transporter.getSpecialty()){
            throw new RuntimeException("Transporter specialty mismatch! Needs " + colis.getType());
        }

        if(transporter.getStatus() != TransporterStatus.AVAILABLE){
            throw new RuntimeException("Transporter is Not Available");
        }

        colis.setTransportorId(transporterId);
        colis.setStatus(PackageStatus.IN_TRANSIT);

        transporter.setStatus(TransporterStatus.IN_DELIVERY);
        userRepository.save(transporter);

        return packageMapper.toResponse(packageRepository.save(colis));
    }

    public PackageResponse updateStatus(String colisId, PackageStatus newStatus){
        Package colis = packageRepository.findById(colisId)
                .orElseThrow(() -> new RuntimeException("Package Not Found"));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isOwner = colis.getTransportorId() != null && colis.getTransportorId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if(!isAdmin && !isOwner){
            throw new RuntimeException("Not Authorized");
        }

        colis.setStatus(newStatus);

        if(newStatus == PackageStatus.DELIVERED && colis.getTransportorId() != null){
            userRepository.findById(colis.getTransportorId()).ifPresent(t -> {
                t.setStatus(TransporterStatus.AVAILABLE);
                userRepository.save(t);
            });
        }

        return packageMapper.toResponse(packageRepository.save(colis));
    }

    public Page<PackageResponse> findAllPaginated(int page, int size, Specialty type,
                                                  PackageStatus statut, String adresseContains) {

        Pageable pageable = PageRequest.of(page, size);

        Query query = new Query().with(pageable);

        if (type != null) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        if (statut != null) {
            query.addCriteria(Criteria.where("status").is(statut));
        }
        if (adresseContains != null && !adresseContains.isEmpty()) {
            query.addCriteria(Criteria.where("adresseDestination")
                    .regex(adresseContains, "i")); // case-insensitive
        }

        List<Package> results = mongoTemplate.find(query, Package.class);
        long total = mongoTemplate.count(query.skip(-1).limit(-1), Package.class);

        Page<PackageResponse> pageResult = new PageImpl<>(
                results.stream().map(packageMapper::toResponse).toList(),
                pageable,
                total
        );

        return pageResult;
    }

    public Page<PackageResponse> findMyPackages(String transporterId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return packageRepository.findByTransportorId(transporterId, pageable)
                .map(packageMapper::toResponse);
    }

}
