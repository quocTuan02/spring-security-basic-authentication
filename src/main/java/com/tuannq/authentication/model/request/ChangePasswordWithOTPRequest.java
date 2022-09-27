package com.tuannq.authentication.model.request;

import com.tuannq.authentication.annotation.Email;
import com.tuannq.authentication.annotation.FieldMatch;
import com.tuannq.authentication.annotation.Password;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldMatch(first = "confirmPassword", second = "newPassword", message = "password.not-match")
public class ChangePasswordWithOTPRequest {
    @NotNull(message = "not-null")
    @NotEmpty(message = "not-null")
    @Email
    @ApiModelProperty(
            example = "user@tuannq.com",
            notes = "not-null",
            required = true
    )
    private String email;

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

    @NotNull(message = "not-null")
    @NotEmpty(message = "not-null")
    @ApiModelProperty(
            example = "456789",
            notes = "not-null",
            required = true
    )
    private String otp;


}
