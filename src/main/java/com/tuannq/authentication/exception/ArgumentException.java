package com.tuannq.authentication.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArgumentException extends RuntimeException {
  private String field;

  public ArgumentException(String field, String message) {
    super(message);
    this.field = field;
  }
}
