package com.logistics.sdg.repository;

import com.logistics.sdg.model.User;
import com.logistics.sdg.model.enums.Role;
import com.logistics.sdg.model.enums.Specialty;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByLogin(String login);

    List<User> findAllByRole(Role role);

    List<User> findAllByRoleAndSpecialty(Role role, Specialty specialty);
}