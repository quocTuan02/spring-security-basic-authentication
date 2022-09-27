package com.tuannq.authentication.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse<T> extends AbstractResponse {
  private T data;

  public SuccessResponse(String message, T data) {
    super(false, message);
    this.data = data;
  }
}
