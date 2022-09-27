package com.tuannq.authentication.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractResponse {
    private boolean hasError;
    private String message;
}
