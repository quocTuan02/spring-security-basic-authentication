package com.tuannq.authentication.model.request;

import com.tuannq.authentication.annotation.Email;
import com.tuannq.authentication.annotation.Phone;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateProfileForm {
    @NotNull(message = "not-null")
    @NotEmpty(message = "not-null")
    @Size(min = 1, max = 255, message = "size-1-255")
    @ApiModelProperty(
            example = "Nguyễn Anh Tùng",
            notes = "not-null",
            required = true
    )
    private String fullName;

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

    @NotNull(message = "not-null")
    @NotEmpty(message = "not-null")
    @Pattern(regexp = "[012]", message = "gender.invalid")
    @ApiModelProperty(
            example = "0",
            notes = "not-null",
            required = true
    )
    private String gender;

    @NotNull(message = "not-null")
    @NotEmpty(message = "not-null")
    @Size(min = 5, max = 511, message = "size-5-511")
    @ApiModelProperty(
            example = "Thiệu Ngọc, Thiệu Hóa, Thanh Hóa.",
            notes = "not-null",
            required = true
    )
    private String address;


}
