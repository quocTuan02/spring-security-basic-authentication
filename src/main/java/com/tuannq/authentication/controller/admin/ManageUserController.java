package com.tuannq.authentication.controller.admin;

import com.tuannq.authentication.entity.Users;
import com.tuannq.authentication.exception.ArgumentException;
import com.tuannq.authentication.exception.BadRequestException;
import com.tuannq.authentication.exception.NotFoundException;
import com.tuannq.authentication.model.dto.UserDTO;
import com.tuannq.authentication.model.request.UserFormAdmin;
import com.tuannq.authentication.model.request.UserSearchForm;
import com.tuannq.authentication.model.response.SuccessResponse;
import com.tuannq.authentication.model.type.StatusType;
import com.tuannq.authentication.model.type.UserType;
import com.tuannq.authentication.security.CustomUserDetails;
import com.tuannq.authentication.service.UserService;
import com.tuannq.authentication.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class ManageUserController {
    private final AuthUtils authUtils;
    private final UserService userService;
    private final MessageSource messageSource;

    @GetMapping("/admin/users")
    public String getAll(
            Model model,
            UserSearchForm form
    ) {
        var identity = authUtils.getUser().get();
        var users = userService.search(form);
        model.addAttribute("identity", new UserDTO(identity));
        model.addAttribute("users", users);
        model.addAttribute("formSearch", form);
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("currentPage", users.getCurrentPage());

        return "admin/user/list";
    }

    @GetMapping("/api/admin/users/{id}")
    public ResponseEntity<SuccessResponse<UserDTO>> getUser(
            @PathVariable long id
    ) throws ArgumentException {

        var userOpt = userService.findById(id);
        if (userOpt.isEmpty())
            throw new NotFoundException(messageSource.getMessage("not-found.user.id", null, LocaleContextHolder.getLocale()).concat(String.valueOf(id)));

        return ResponseEntity.ok(new SuccessResponse<>(
                null,
                new UserDTO(userOpt.get())
        ));
    }

    @PostMapping("/api/admin/users")
    public ResponseEntity<SuccessResponse<UserDTO>> addUser(
            @Validated @RequestBody UserFormAdmin form
    ) throws ArgumentException, MessagingException, UnsupportedEncodingException {
        Users user = userService.addUserByAdmin(form);
        return ResponseEntity.ok(new SuccessResponse<>(
                messageSource.getMessage("add.success", null, LocaleContextHolder.getLocale()),
                new UserDTO(user)
        ));
    }

    @PutMapping("/api/admin/users/{id}")
    public ResponseEntity<SuccessResponse<UserDTO>> updateUser(
            @Validated @RequestBody UserFormAdmin form,
            @PathVariable long id
    ) throws ArgumentException {

        var userOpt = userService.findById(id);
        if (userOpt.isEmpty())
            throw new NotFoundException(messageSource.getMessage("not-found.user.id", null, LocaleContextHolder.getLocale()).concat(String.valueOf(id)));
        var identity = authUtils.getUser().get();

        if (identity.getId() == id) {
            if (form.getIsDeleted()
                    || Objects.equals(form.getRole(), identity.getRole())
            ) {
                throw new BadRequestException(messageSource.getMessage("update.fail", null, LocaleContextHolder.getLocale()));
            }
            if (UserType.ADMIN.getRole().equalsIgnoreCase(identity.getRole())) {
                if (!form.getStatus().equalsIgnoreCase(StatusType.CLOSED.name())) {
                    throw new ArgumentException("status", "status.invalid");
                }
            }
        }

        Users user = userService.editUserByAdmin(userOpt.get(), form);

        if (identity.getId().equals(user.getId())) {
            UserDetails principal = new CustomUserDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return ResponseEntity.ok(new SuccessResponse<>(
                messageSource.getMessage("update.success", null, LocaleContextHolder.getLocale()),
                new UserDTO(user)
        ));
    }

    @DeleteMapping("/api/admin/users/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable long id
    ) {
        var identity = authUtils.getUser().get();
        if (identity.getId() == id)
            return ResponseEntity.badRequest().body(new SuccessResponse<>(messageSource.getMessage("delete.fail", null, LocaleContextHolder.getLocale()), null));

        userService.deleteById(id);

        return ResponseEntity.ok(new SuccessResponse<>(messageSource.getMessage("delete.success", null, LocaleContextHolder.getLocale()), null));
    }

}
