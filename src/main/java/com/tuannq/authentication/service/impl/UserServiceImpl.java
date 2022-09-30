package com.tuannq.authentication.service.impl;

import com.tuannq.authentication.entity.OTP;
import com.tuannq.authentication.entity.Users;
import com.tuannq.authentication.exception.ArgumentException;
import com.tuannq.authentication.exception.NotFoundException;
import com.tuannq.authentication.model.dto.UserDTO;
import com.tuannq.authentication.model.request.*;
import com.tuannq.authentication.model.response.PageResponse;
import com.tuannq.authentication.model.type.TransactionType;
import com.tuannq.authentication.repository.OTPRepository;
import com.tuannq.authentication.repository.UserRepository;
import com.tuannq.authentication.service.MailService;
import com.tuannq.authentication.service.UserService;
import com.tuannq.authentication.util.ConverterUtils;
import com.tuannq.authentication.util.PageUtil;
import lombok.RequiredArgsConstructor;
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

import static com.tuannq.authentication.config.DefaultVariable.LIMIT;


@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final OTPRepository otpRepository;

    @Override
    public PageResponse<UserDTO> search(UserSearchForm form) {
        var data =  userRepository.search(form,  new PageUtil(form.getPage(), LIMIT, form.getOrder(), form.getDirection()).getPageRequest())
                .map(UserDTO::new);
        return new PageResponse<>(data);
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
    public Users changePasswordWithOTP(ChangePasswordWithOTPRequest form) {
        Users user = userRepository.findByEmail(form.getEmail());
        if (user == null)
            throw new ArgumentException("email", messageSource.getMessage("email.not-found", null, LocaleContextHolder.getLocale()));
        var otpOptional = otpRepository.findTransactionAndEmail(TransactionType.FORGOT_PASSWORD.name(), form.getEmail());
        checkOTP(otpOptional, form.getOtp());

        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Users addUserByAdmin(UserFormAdmin form) throws ArgumentException, MessagingException, UnsupportedEncodingException {
        if (userRepository.findByPhone(form.getPhone()) != null)
            throw new ArgumentException("phone", messageSource.getMessage("phone.exist", null, LocaleContextHolder.getLocale()));
        if (userRepository.findByEmail(form.getEmail()) != null)
            throw new ArgumentException("email", messageSource.getMessage("email.exist", null, LocaleContextHolder.getLocale()));

        String pwd = ConverterUtils.generateRandomPassword();
        Users user = new Users(form, passwordEncoder.encode(pwd));

        mailService.sendPassword(user.getEmail(), pwd);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Users editUserByAdmin(Users user, UserFormAdmin form) throws ArgumentException {
        Users u1 = userRepository.findByPhone(form.getPhone());
        if (u1 != null && !u1.getId().equals(user.getId()))
            throw new ArgumentException("phone", messageSource.getMessage("phone.exist", null, LocaleContextHolder.getLocale()));
        Users u2 = userRepository.findByEmail(form.getEmail());
        if (u2 != null && !u2.getId().equals(user.getId()))
            throw new ArgumentException("email", messageSource.getMessage("email.exist", null, LocaleContextHolder.getLocale()));

        user.setUser(form);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Users updateProfile(Users user, UpdateProfileForm form) throws ArgumentException {
        Users u1 = userRepository.findByPhone(form.getPhone());
        if (u1 != null && !u1.getId().equals(user.getId()))
            throw new ArgumentException("phone", messageSource.getMessage("phone.exist", null, LocaleContextHolder.getLocale()));
        Users u2 = userRepository.findByEmail(form.getEmail());
        if (u2 != null && !u2.getId().equals(user.getId()))
            throw new ArgumentException("email", messageSource.getMessage("email.exist", null, LocaleContextHolder.getLocale()));

        user.setUser(form);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Authentication authenticate(LoginForm form) {
        Users ue = userRepository.findExistByUsernameIgnoreCaseOrEmailIgnoreCase(form.getUsername());
        if (ue == null)
            throw new BadCredentialsException(messageSource.getMessage("info-login.incorrect", null, LocaleContextHolder.getLocale()));

        if (!passwordEncoder.matches(form.getPassword(), ue.getPassword()))
            throw new BadCredentialsException(messageSource.getMessage("info-login.incorrect", null, LocaleContextHolder.getLocale()));


        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        form.getUsername(),
                        form.getPassword()
                )
        );
    }

    @Override
    public Optional<Users> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Users addUserByCustomer(UserFormCustomer form) throws ArgumentException {
        if (userRepository.findByPhone(form.getPhone()) != null)
            throw new ArgumentException("phone", messageSource.getMessage("phone.exist", null, LocaleContextHolder.getLocale()));
        if (userRepository.findByEmail(form.getEmail()) != null)
            throw new ArgumentException("email", messageSource.getMessage("email.exist", null, LocaleContextHolder.getLocale()));

        Users user = new Users(form, passwordEncoder.encode(form.getPassword()));
        return userRepository.save(user);
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
