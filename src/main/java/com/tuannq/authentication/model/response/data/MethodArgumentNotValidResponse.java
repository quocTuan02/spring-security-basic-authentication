package com.tuannq.authentication.model.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MethodArgumentNotValidResponse {
    private String field;
    private String message;
}
