package com.example.demo.configuaration;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserRepository userRepository;

    // code mặc định run ứng nếu không thấy tài khoản admin thì mặc định tạo tài khoản (focus log)
    @Bean
    public ApplicationRunner adminInitializer() {
        return args -> {
            String adminUsername = "admin";
            String adminPassword = "admin123"; // Nên sử dụng mã hóa mật khẩu
            Set<Role> adminRole = new HashSet<>();
            adminRole.add(roleRepository.findByName("ADMIN"));

            // Kiểm tra xem admin đã tồn tại chưa
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                User admin = User.builder()
                        .username(adminUsername)
                        .password(passwordEncoder.encode(adminPassword))
                        .roles(adminRole)
                        .build();
                log.info("Toi dang tao admin");
                userRepository.save(admin);
            }
        };
    }
}
