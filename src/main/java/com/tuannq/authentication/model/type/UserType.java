package com.tuannq.authentication.model.type;

public enum UserType {

    ADMIN ("ADMIN", "ROLE_ADMIN"),
    EMPLOYEE ("EMPLOYEE", "ROLE_EMPLOYEE"),
    USER("USER", "ROLE_USER"),
    ANONYMOUS("ANONYMOUS", "ROLE_ANONYMOUS");

    private final String role;
    private final String fullRole;

    UserType(String role, String fullRole) {
        this.role = role;
        this.fullRole = fullRole;
    }

    public String getRole() {
        return role;
    }

    public String getFullRole() {
        return fullRole;
    }
}
