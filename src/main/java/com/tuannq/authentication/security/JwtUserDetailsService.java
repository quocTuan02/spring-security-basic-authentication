package com.tuannq.authentication.security;

import com.tuannq.authentication.entity.Users;
import com.tuannq.authentication.model.type.StatusType;
import com.tuannq.authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users user = userRepository.findExistByUsernameOrEmailIgnoreCase(s);
        if (user == null) return null;
        if (!StatusType.CLOSED.name().equalsIgnoreCase(user.getStatus())){
            throw new BadCredentialsException("Tài khoản chưa được kích hoạt. Vui lòng liên hệ ADMIN");
        }
        return new CustomUserDetails(user);
    }
}
