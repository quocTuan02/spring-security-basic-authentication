package com.tuannq.authentication;

import com.tuannq.authentication.entity.Users;
import com.tuannq.authentication.model.type.UserType;
import com.tuannq.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class Application {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init() {
        var user = userRepository.findExistByUsernameOrEmailIgnoreCase("demo");
        if (user == null)
            userRepository.save(
                    new Users("demo",
                            "Demo",
                            "demo@yopmai.com",
                            passwordEncoder.encode("demo"),
                            "",
                            "",
                            UserType.ADMIN.getRole()
                    ));
    }
}
