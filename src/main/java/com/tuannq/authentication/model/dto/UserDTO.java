package com.tuannq.authentication.model.dto;

import com.tuannq.authentication.entity.Users;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO extends AbstractDTO {
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String address;

    private String role;
    private String status;
    public UserDTO(Users user) {
        super(
                user.getId(),
                user.getCreatedBy(),
                user.getCreatedAt(),
                user.getModifiedBy(),
                user.getModifiedAt(),
                user.getIsDeleted()
        );
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.status = user.getStatus();
    }
}
