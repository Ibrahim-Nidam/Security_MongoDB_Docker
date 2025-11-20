package com.logistics.sdg.repository;

import com.logistics.sdg.model.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PackageRepository extends MongoRepository<Package, String> {
    Page<Package> findByTransportorId(String transporterId, Pageable pageable);
}
