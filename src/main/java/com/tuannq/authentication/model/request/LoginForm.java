package com.tuannq.authentication.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginForm {
  @NotNull(message = "not-null")
  @NotEmpty(message = "not-null")
  @ApiModelProperty(
      example = "admin@tuannq.com",
      notes = "not-null",
      required = true
  )
  private String username;

  @NotNull(message = "not-null")
  @NotEmpty(message = "not-null")
  @ApiModelProperty(
      example = "123456",
      notes = "not-null",
      required = true
  )
  private String password;
}
