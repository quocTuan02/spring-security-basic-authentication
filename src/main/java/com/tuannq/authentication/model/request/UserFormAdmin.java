package com.tuannq.authentication.model.request;

import com.tuannq.authentication.annotation.Phone;
import com.tuannq.authentication.annotation.Role;
import com.tuannq.authentication.annotation.Status;
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
public class UserFormAdmin {
    @NotNull(message = "not-null")
    @NotEmpty(message = "not-null")
    @Size(min = 1, max = 255, message = "size-1-255")
    @ApiModelProperty(
            example = "Nguyễn Anh Tùng",
            notes = "not-null",
            required = true
    )
    private String fullName;

    @NotBlank(message = "not-null")
    @Pattern(regexp = "^[a-zA-Z]([._-](?![._-])|[a-zA-Z0-9]){1,18}[a-zA-Z0-9]$", message = "\n^[a-zA-Z]([._-](?![._-])|[a-zA-Z0-9]){1,18}[a-zA-Z0-9]$")
    @ApiModelProperty(
            example = "TuanNQ",
            notes = "not-null",
            required = true
    )
    private String username;

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
            example = "0916016972",
            notes = "not-null",
            required = true
    )
    private String phone;

    @NotNull(message = "not-null")
    @NotEmpty(message = "not-null")
    @Role
    @ApiModelProperty(
            example = "[USER]",
            notes = "not-null",
            required = true
    )
    private String role;

    @NotBlank(message = "not-null")
    @Status
    @ApiModelProperty(
            example = "[OPEN]",
            notes = "not-null",
            required = true
    )
    private String status;

    @Size(max = 511, message = "size-5-511")
    @ApiModelProperty(
            example = "Thiệu Ngọc, Thiệu Hóa, Thanh Hóa.",
            notes = "not-null",
            required = true
    )
    private String address;

    @Size(max = 511, message = "size-5-511")
    @ApiModelProperty(
            example = "Thiệu Ngọc, Thiệu Hóa, Thanh Hóa.",
            notes = "not-null",
            required = true
    )
    private String note;

    @ApiModelProperty(
            example = "false"
    )
    private Boolean isDeleted = false;
}
