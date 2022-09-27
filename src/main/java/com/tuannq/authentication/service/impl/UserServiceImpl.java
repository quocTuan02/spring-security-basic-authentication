package com.tuannq.authentication.service.impl;

import com.tuannq.authentication.entity.OTP;
import com.tuannq.authentication.entity.Users;
import com.tuannq.authentication.exception.ArgumentException;
import com.tuannq.authentication.exception.NotFoundException;
import com.tuannq.authentication.model.request.*;
import com.tuannq.authentication.model.type.TransactionType;
import com.tuannq.authentication.repository.OTPRepository;
import com.tuannq.authentication.repository.UserRepository;
import com.tuannq.authentication.service.MailService;
import com.tuannq.authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;


@Component
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final OTPRepository otpRepository;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            MailService mailService,
            MessageSource messageSource,
            AuthenticationManager authenticationManager,
            OTPRepository otpRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.messageSource = messageSource;
    }


    @Override
    public void deleteById(long id) {
        var userOpt = userRepository.findById(id);
        if (userOpt.isEmpty())
            throw new NotFoundException(messageSource.getMessage("not-found.user.id", null, LocaleContextHolder.getLocale()).concat(String.valueOf(id)));

        userRepository.deleteById(id);
    }

    @Override
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public String sendOTPToEmail(String email) throws MessagingException, UnsupportedEncodingException {
        Users user = userRepository.findByEmail(email);
        if (user == null)
            throw new ArgumentException("email", messageSource.getMessage("email.not-found", null, LocaleContextHolder.getLocale()));
        var otp = generateOTP(TransactionType.FORGOT_PASSWORD, email);

        mailService.sendOTP(email, otp);
        return email;
    }

    @Transactional
    @Override
    public void changePasswordWithOTP(ChangePasswordWithOTPRequest form) {
        Users user = userRepository.findByEmail(form.getEmail());
        if (user == null)
            throw new ArgumentException("email", messageSource.getMessage("email.not-found", null, LocaleContextHolder.getLocale()));
        var otpOptional = otpRepository.findTransactionAndEmail(TransactionType.FORGOT_PASSWORD.name(), form.getEmail());
        checkOTP(otpOptional, form.getOtp());

        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public Users register(UserForm form) {
        var password = passwordEncoder.encode(form.getPassword());
        var user = new Users(form, password);
        if (userRepository.findExistByEmail(form.getEmail()) != null)
            throw new ArgumentException("phone", messageSource.getMessage("phone.exist", null, LocaleContextHolder.getLocale()));
        if (userRepository.findExistByUsername(form.getUsername()) != null)
            throw new ArgumentException("username", messageSource.getMessage("username.exist", null, LocaleContextHolder.getLocale()));
        return userRepository.save(user);
    }


    @Transactional
    @Override
    public Authentication authenticate(LoginForm form) {
        Users ue = userRepository.findExistByEmail(form.getEmail());
        if (ue == null)
            throw new BadCredentialsException(messageSource.getMessage("info-login.incorrect", null, LocaleContextHolder.getLocale()));

        if (!passwordEncoder.matches(form.getPassword(), ue.getPassword()))
            throw new BadCredentialsException(messageSource.getMessage("info-login.incorrect", null, LocaleContextHolder.getLocale()));


        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        form.getEmail(),
                        form.getPassword()
                )
        );
    }

    @Override
    public Optional<Users> findById(long id) {
        return userRepository.findById(id);
    }


    @Override
    public void changePassword(Users user, ChangePasswordForm form) throws ArgumentException {
        // Validate password
        if (!passwordEncoder.matches(form.getOldPassword(), user.getPassword()))
            throw new ArgumentException("oldPassword", messageSource.getMessage("password.incorrect", null, LocaleContextHolder.getLocale()));

        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);
    }

    private String generateOTP(TransactionType transactionType, String email) {
        final String number = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(number.length());
            sb.append(number.charAt(randomIndex));
        }
        var otp = sb.toString();
        otpRepository.save(
                new OTP(transactionType.name(),
                        passwordEncoder.encode(otp),
                        null,
                        email,
                        Instant.ofEpochMilli(System.currentTimeMillis() + 5 * 60 * 1000)
                )
        );

        return otp;
    }

    void checkOTP(Optional<OTP> otpOptional, String str) throws ArgumentException {
        var checkOTP = otpOptional.map(otp -> passwordEncoder.matches(str, otp.getOtp())
                && !Instant.now().isAfter(otp.getExpiration())
                && otp.getTotalRequest() < 100).orElse(false);
        otpOptional.map(otp -> {
            otp.setTotalRequest(otp.getTotalRequest() + 1);
            if (checkOTP) otp.setIsDeleted(true);
            return otp;
        }).map(otpRepository::save);
        if (!checkOTP) {
            throw new ArgumentException("otp", messageSource.getMessage("otp.expired", null, LocaleContextHolder.getLocale()));
        }
    }
}
