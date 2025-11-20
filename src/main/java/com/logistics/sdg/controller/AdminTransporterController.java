package com.logistics.sdg.controller;

import com.logistics.sdg.dto.transporter.CreateTransporterRequest;
import com.logistics.sdg.dto.transporter.TransporterResponse;
import com.logistics.sdg.exception.BusinessException;
import com.logistics.sdg.mapper.TransporterMapper;
import com.logistics.sdg.model.User;
import com.logistics.sdg.model.enums.Role;
import com.logistics.sdg.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/transporters")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTransporterController {
    private final UserRepository userRepository;
    private final TransporterMapper transporterMapper;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public TransporterResponse create(@RequestBody CreateTransporterRequest request){
        if(userRepository.findByLogin(request.login()).isPresent()){
            throw new BusinessException("login already exists");
        }

        User user = transporterMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        return transporterMapper.toResponse(userRepository.save(user));
    }

    @GetMapping
    public List<TransporterResponse> list(){
        return userRepository.findAllByRole(Role.TRANSPORTOR).stream()
                .map(transporterMapper::toResponse)
                .toList();
    }

    @PatchMapping("/{id}/toggle-active")
    public TransporterResponse toggleActive(@PathVariable String id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Transporter Not Found"));

        user.setActive(!user.isActive());
        return transporterMapper.toResponse(userRepository.save(user));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        userRepository.deleteById(id);
    }
}
