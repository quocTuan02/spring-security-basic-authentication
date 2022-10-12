package com.tuannq.authentication.service.impl;

import com.tuannq.authentication.entity.OTP;
import com.tuannq.authentication.entity.Users;
import com.tuannq.authentication.exception.ArgumentException;
import com.tuannq.authentication.exception.NotFoundException;
import com.tuannq.authentication.model.dto.UserDTO;
import com.tuannq.authentication.model.request.*;
import com.tuannq.authentication.model.response.PageResponse;
import com.tuannq.authentication.model.type.StatusType;
import com.tuannq.authentication.model.type.TransactionType;
import com.tuannq.authentication.model.type.UserType;
import com.tuannq.authentication.repository.OTPRepository;
import com.tuannq.authentication.repository.UserRepository;
import com.tuannq.authentication.service.MailService;
import com.tuannq.authentication.service.UserService;
import com.tuannq.authentication.util.AuthUtils;
import com.tuannq.authentication.util.ConverterUtils;
import com.tuannq.authentication.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
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
    private final AuthUtils authUtils;

    @Override
    public PageResponse<UserDTO> search(UserSearchForm form) {
        var role = authUtils.getUser()
                .map(Users::getRole)
                .orElse("");
        var data = userRepository.search(
                role,
                form,
                new PageUtil(form.getPage(), LIMIT, form.getOrder(), form.getDirection()).getPageRequest()
        ).map(UserDTO::new);
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
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Transactional
    @Override
    public String sendOTPToEmail(String email) throws MessagingException, UnsupportedEncodingException {
        Users user = userRepository.findByEmailIgnoreCase(email);
        if (user == null)
            throw new ArgumentException("email", messageSource.getMessage("email.not-found", null, LocaleContextHolder.getLocale()));
        var otp = generateOTP(TransactionType.FORGOT_PASSWORD, email);

        mailService.sendOTP(email, otp);
        return email;
    }

    @Transactional
    @Override
    public Users changePasswordWithOTP(ChangePasswordWithOTPRequest form) {
        Users user = userRepository.findByEmailIgnoreCase(form.getEmail());
        if (user == null)
            throw new ArgumentException("email", messageSource.getMessage("email.not-found", null, LocaleContextHolder.getLocale()));
        var otpOptional = otpRepository.findTransactionAndEmail(TransactionType.FORGOT_PASSWORD.name(), form.getEmail());
        checkOTP(otpOptional, form.getOtp());

        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Users addUserByAdmin(UserFormAdmin form) throws ArgumentException {
        if (Strings.isNotBlank(form.getPhone()) && userRepository.findByPhone(form.getPhone()) != null)
            throw new ArgumentException("phone", messageSource.getMessage("phone.exist", null, LocaleContextHolder.getLocale()));
        if (Strings.isNotBlank(form.getUsername()) && userRepository.findByUsername(form.getUsername()) != null)
            throw new ArgumentException("username", "username.exist");
        if (userRepository.findByEmailIgnoreCase(form.getEmail()) != null)
            throw new ArgumentException("email", messageSource.getMessage("email.exist", null, LocaleContextHolder.getLocale()));

        String pwd = ConverterUtils.generateRandomPassword();
        Users user = new Users(form, passwordEncoder.encode(pwd));
        var identity = authUtils.getUser().get();
        if (!UserType.ADMIN.getRole().equalsIgnoreCase(identity.getRole())) {
            if (form.getStatus().equalsIgnoreCase(StatusType.CLOSED.name())) {
                throw new ArgumentException("status", "status.invalid");
            }
        }
        if (Strings.isBlank(form.getStatus())) {
            user.setStatus(StatusType.OPEN.name());
        }

//        mailService.sendPassword(user.getEmail(), pwd);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Users editUserByAdmin(Users user, UserFormAdmin form) throws ArgumentException {
        Users u1 = null;
        if (Strings.isNotBlank(form.getPhone())) {
            u1 = userRepository.findByPhone(form.getPhone());
        }
        if (u1 != null && !u1.getId().equals(user.getId()))
            throw new ArgumentException("phone", messageSource.getMessage("phone.exist", null, LocaleContextHolder.getLocale()));
        Users u2 = userRepository.findByEmailIgnoreCase(form.getEmail());
        if (u2 != null && !u2.getId().equals(user.getId()))
            throw new ArgumentException("email", messageSource.getMessage("email.exist", null, LocaleContextHolder.getLocale()));

        Users u3 = userRepository.findByUsername(form.getUsername());
        if (u3 != null && !u3.getId().equals(user.getId()))
            throw new ArgumentException("username", "username.exist");

        user.setUser(form);
        var identity = authUtils.getUser().get();
        if (!UserType.ADMIN.getRole().equalsIgnoreCase(identity.getRole())) {
            if (form.getStatus().equalsIgnoreCase(StatusType.CLOSED.name())) {
                throw new ArgumentException("status", "Vui lòng chuyển trạng thái qua khác CLOSED để ADMIN duyệt lại!");
            }
            if (user.getStatus().equalsIgnoreCase(StatusType.CLOSED.name())) {
                user.setStatus(StatusType.REOPEN.name());
            }
        }

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Users updateProfile(Users user, UpdateProfileForm form) throws ArgumentException {
        Users u1 = null;
        if (Strings.isNotBlank(form.getPhone())) {
            u1 = userRepository.findByPhone(form.getPhone());
        }

        if (u1 != null && !u1.getId().equals(user.getId()))
            throw new ArgumentException("phone", messageSource.getMessage("phone.exist", null, LocaleContextHolder.getLocale()));
        Users u2 = userRepository.findByEmailIgnoreCase(form.getEmail());
        if (u2 != null && !u2.getId().equals(user.getId()))
            throw new ArgumentException("email", messageSource.getMessage("email.exist", null, LocaleContextHolder.getLocale()));
        Users u3 = userRepository.findByUsername(form.getUsername());
        if (u3 != null && !u3.getId().equals(user.getId()))
            throw new ArgumentException("username", "username.exist");

        user.setUser(form);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Authentication authenticate(LoginForm form) {
        Users ue = userRepository.findExistByUsernameOrEmailIgnoreCase(form.getUsername());
        if (ue == null)
            throw new BadCredentialsException(messageSource.getMessage("info-login.incorrect", null, LocaleContextHolder.getLocale()));

        if (!passwordEncoder.matches(form.getPassword(), ue.getPassword()))
            throw new BadCredentialsException(messageSource.getMessage("info-login.incorrect", null, LocaleContextHolder.getLocale()));


        if (!StatusType.CLOSED.name().equalsIgnoreCase(ue.getStatus())){
            throw new BadCredentialsException("Tài khoản chưa được kích hoạt. Vui lòng liên hệ ADMIN");
        }
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        form.getUsername(),
                        form.getPassword()
                )
        );
    }

    @Override
    public Optional<Users> findById(long id) {
        var rs = userRepository.findById(id);

        var identity = authUtils.getUser().get();
        if (UserType.ADMIN.getRole().equalsIgnoreCase(identity.getRole())) {
            return rs;
        }
        var c = rs.map(Users::getRole)
                .map(a -> UserType.USER.getRole().equalsIgnoreCase(a))
                .orElse(false);
        if (c) {
            return rs;
        }
        return Optional.empty();
    }

    @Override
    public Users addUserByCustomer(UserFormCustomer form) throws ArgumentException {
        if (Strings.isNotBlank(form.getPhone()) && userRepository.findByPhone(form.getPhone()) != null)
            throw new ArgumentException("phone", messageSource.getMessage("phone.exist", null, LocaleContextHolder.getLocale()));
        if (userRepository.findByEmailIgnoreCase(form.getEmail()) != null)
            throw new ArgumentException("email", messageSource.getMessage("email.exist", null, LocaleContextHolder.getLocale()));

        Users user = new Users(form, passwordEncoder.encode(form.getPassword()));
        user.setStatus(StatusType.OPEN.name());
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
