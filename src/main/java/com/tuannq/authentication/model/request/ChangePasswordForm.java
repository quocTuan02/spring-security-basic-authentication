package com.tuannq.authentication.model.request;

import com.tuannq.authentication.annotation.FieldMatch;
import com.tuannq.authentication.annotation.Password;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(first = "confirmPassword", second = "newPassword", message = "password.not-match")
public class ChangePasswordForm {
    @NotNull(message = "not-null")
    @NotEmpty(message = "not-null")
    @ApiModelProperty(
            example = "User@123",
            notes = "not-null",
            required = true
    )
    private String oldPassword;

    @NotNull(message = "not-null")
    @NotEmpty(message = "not-null")
    @Password
    @ApiModelProperty(
            example = "User@123",
            notes = "not-null",
            required = true
    )
    private String newPassword;

    @NotNull(message = "not-null")
    @NotEmpty(message = "not-null")
    @Password
    @ApiModelProperty(
            example = "User@123",
            notes = "not-null",
            required = true
    )
    private String confirmPassword;
}
