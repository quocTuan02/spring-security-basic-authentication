package com.tuannq.authentication.controller.anonymous;

import com.tuannq.authentication.exception.ArgumentException;
import com.tuannq.authentication.model.dto.UserDTO;
import com.tuannq.authentication.model.request.*;
import com.tuannq.authentication.model.response.SuccessResponse;
import com.tuannq.authentication.security.CustomUserDetails;
import com.tuannq.authentication.security.JwtTokenUtil;
import com.tuannq.authentication.service.UserService;
import com.tuannq.authentication.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final MessageSource messageSource;
    private final AuthUtils authUtils;

    @PostMapping("/api/change-password")
    public ResponseEntity<SuccessResponse<UserDTO>> changePassword(
            @Validated @RequestBody ChangePasswordForm form,
            HttpServletResponse httpResponse
    ) throws ArgumentException, IOException {
        var userOpt = authUtils.getUser();
        if (userOpt.isEmpty()) {
            httpResponse.sendRedirect("/login");
            return null;
        }
        userService.changePassword(userOpt.get(), form);
        return ResponseEntity.ok(new SuccessResponse<>(
                messageSource.getMessage("register.success", null, LocaleContextHolder.getLocale()),
                null
        ));
    }


    @GetMapping("/account")
    public String account(
            HttpServletResponse httpResponse
    ) throws IOException {
        var userOpt = authUtils.getUser();
        if (userOpt.isEmpty()) {
            httpResponse.sendRedirect("/login");
            return null;
        }

        httpResponse.sendRedirect("/");
        return null;
    }

    @GetMapping("/account/profile")
    public String profile(
            Model model,
            HttpServletResponse httpResponse
    ) throws IOException {
        var userOpt = authUtils.getUser();

        if (userOpt.isEmpty()) {
            httpResponse.sendRedirect("/login");
            return null;
        }
        model.addAttribute("user", userOpt.get());
        return "account/profile";
    }

    @GetMapping("/account/change-password")
    public String changePassword(
            Model model,
            HttpServletResponse httpResponse
    ) throws IOException {
        var userOpt = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .map(u -> {
                    if (u instanceof CustomUserDetails)
                        return ((CustomUserDetails) u).getUser();
                    else return null;
                });
        if (userOpt.isEmpty()) {
            httpResponse.sendRedirect("/login");
            return null;
        }
        return "account/change-password";
    }
}
