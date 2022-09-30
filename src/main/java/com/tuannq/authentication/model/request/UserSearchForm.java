package com.tuannq.authentication.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class UserSearchForm extends SearchPage{
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String roles;
}
