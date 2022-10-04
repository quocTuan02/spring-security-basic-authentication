package com.tuannq.authentication;

import com.tuannq.authentication.annotation.Status;
import com.tuannq.authentication.entity.Users;
import com.tuannq.authentication.model.type.StatusType;
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
        if (user == null) {
            userRepository.save(
                    new Users("demo",
                            "Demo",
                            "demo@yopmai.com",
                            passwordEncoder.encode("demo"),
                            "",
                            "",
                            UserType.ADMIN.getRole(),
                            StatusType.CLOSED.name()
                    )
            );
        }

        user = userRepository.findExistByUsernameOrEmailIgnoreCase("employee");
        if (user == null) {
            userRepository.save(
                    new Users("employee",
                            "employee",
                            "employee@yopmai.com",
                            passwordEncoder.encode("demo"),
                            "",
                            "",
                            UserType.EMPLOYEE.getRole(),
                            StatusType.CLOSED.name()
                    )
            );
        }

        user = userRepository.findExistByUsernameOrEmailIgnoreCase("user");
        if (user == null) {
            userRepository.save(
                    new Users("user",
                            "user",
                            "user@yopmai.com",
                            passwordEncoder.encode("demo"),
                            "",
                            "",
                            UserType.USER.getRole(),
                            StatusType.CLOSED.name()
                    )
            );
        }

    }
}
