package com.logistics.sdg.config;

import com.logistics.sdg.model.User;
import com.logistics.sdg.model.enums.Role;
import com.logistics.sdg.model.enums.Specialty;
import com.logistics.sdg.model.enums.TransporterStatus;
import com.logistics.sdg.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String ...args){
        if(userRepository.findByLogin("admin").isEmpty()){
            User admin = User.builder()
                    .login("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role(Role.ADMIN)
                    .active(true)
                    .build();

            userRepository.save(admin);
            log.info("Test ADMIN created → login: admin / password: admin");
        }

        if(userRepository.findByLogin("port").isEmpty()){
            User porter = User.builder()
                    .login("port")
                    .password(passwordEncoder.encode("port"))
                    .role(Role.TRANSPORTOR)
                    .specialty(Specialty.FRAGILE)
                    .status(TransporterStatus.AVAILABLE)
                    .active(true)
                    .build();

            userRepository.save(porter);
            log.info("Test TRANSPORTER created → login: port / password: port (spécialité FRAGILE)");
        }
    }
}
