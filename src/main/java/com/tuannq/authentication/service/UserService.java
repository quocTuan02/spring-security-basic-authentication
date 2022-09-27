package com.tuannq.authentication.service;

import com.tuannq.authentication.entity.Users;
import com.tuannq.authentication.exception.ArgumentException;
import com.tuannq.authentication.model.request.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public interface UserService {
    Authentication authenticate(LoginForm form);

    Optional<Users> findById(long id);

    void changePassword(Users user, ChangePasswordForm form) throws ArgumentException;


    void deleteById(long id);

    Users findByEmail(String email);

    String sendOTPToEmail(String email) throws MessagingException, UnsupportedEncodingException;

    void changePasswordWithOTP(ChangePasswordWithOTPRequest form);

    Users register(UserForm form);
}
