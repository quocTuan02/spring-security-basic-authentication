package com.tuannq.authentication.service;

import com.tuannq.authentication.entity.Users;
import com.tuannq.authentication.exception.ArgumentException;
import com.tuannq.authentication.model.dto.UserDTO;
import com.tuannq.authentication.model.request.*;
import com.tuannq.authentication.model.response.PageResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public interface UserService {
    Authentication authenticate(LoginForm form);

    Optional<Users> findById(long id);

    Users addUserByCustomer(UserFormCustomer form) throws ArgumentException;

    Users addUserByAdmin(UserFormAdmin form) throws ArgumentException, MessagingException, UnsupportedEncodingException;

    Users editUserByAdmin(Users user, UserFormAdmin form) throws ArgumentException;

    Users updateProfile(Users user, UpdateProfileForm form) throws ArgumentException;

    void changePassword(Users user, ChangePasswordForm form) throws ArgumentException;

    PageResponse<UserDTO> search(UserSearchForm form);

    void deleteById(long id);

    Users findByEmail(String email);

    String sendOTPToEmail(String email) throws MessagingException, UnsupportedEncodingException;

    Users changePasswordWithOTP(ChangePasswordWithOTPRequest form);

}
