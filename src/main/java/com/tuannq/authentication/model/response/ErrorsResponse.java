package com.tuannq.authentication.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorsResponse<T> extends AbstractResponse {
  private T errors;

  public ErrorsResponse(String message, T errors) {
    super(true, message);
    this.errors = errors;
  }
}
