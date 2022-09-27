package com.tuannq.authentication.model.request;

import com.tuannq.authentication.annotation.FieldMatch;
import com.tuannq.authentication.annotation.Password;
import com.tuannq.authentication.annotation.Phone;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldMatch(first = "confirmPassword", second = "password", message = "password.not-match")
public class UserForm {
    @NotBlank(message = "not-null")
    @Pattern(regexp = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$", message = "\n^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$")
    @ApiModelProperty(
            example = "TuanNQ",
            notes = "not-null",
            required = true
    )
    private String username;


    @NotBlank(message = "not-null")
    @Size(min = 1, max = 255, message = "size-1-255")
    @ApiModelProperty(
            example = "Nguyễn Anh Tùng",
            notes = "not-null",
            required = true
    )
    private String fullName;

    @NotNull(message = "not-null")
    @NotEmpty(message = "not-null")
    @Password
    @ApiModelProperty(
            example = "User@123",
            notes = "not-null",
            required = true
    )
    private String password;

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
    @Email
    @ApiModelProperty(
            example = "user@tuannq.com",
            notes = "not-null",
            required = true
    )
    private String email;

    @Phone
    @ApiModelProperty(
            example = "0919234567",
            notes = "not-null",
            required = true
    )
    private String phone;

    @Size(max = 511, message = "size-5-511")
    @ApiModelProperty(
            example = "Thiệu Ngọc, Thiệu Hóa, Thanh Hóa.",
            notes = "not-null",
            required = true
    )
    private String address;
}
