package com.tuannq.authentication.entity;

import com.tuannq.authentication.entity.core.BaseEntity;
import com.tuannq.authentication.model.request.UpdateProfileForm;
import com.tuannq.authentication.model.request.UserFormAdmin;
import com.tuannq.authentication.model.request.UserFormCustomer;
import com.tuannq.authentication.model.type.UserType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@TypeDef(
        name = "json",
        typeClass = JsonStringType.class
)
public class Users extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(unique = true)
    private String phone;
    @Column(nullable = true, length = 511)
    private String address;

    @Type(type = "json")
    @Column(nullable = false, columnDefinition = "json")
    private List<String> roles;

    public void setUser(UserFormAdmin form) {
        this.fullName = form.getFullName();
        this.email = form.getEmail();
        this.phone = form.getPhone();
        this.address = form.getAddress();
        this.roles = form.getRoles();
        this.setIsDeleted(form.getIsDeleted());
    }

    public void setUser(UserFormCustomer form) {
        this.fullName = form.getFullName();
        this.email = form.getEmail();
        this.phone = form.getPhone();
        this.address = form.getAddress();
    }

    public void setUser(UpdateProfileForm form) {
        this.fullName = form.getFullName();
        this.email = form.getEmail();
        this.phone = form.getPhone();
        this.address = form.getAddress();
    }

    public Users(UserFormAdmin form, String password) {
        this.fullName = form.getFullName();
        this.email = form.getEmail();
        this.password = password;
        this.phone = form.getPhone();
        this.address = form.getAddress();
        this.roles = form.getRoles();
    }

    public Users(UserFormCustomer form, String password) {
        this.fullName = form.getFullName();
        this.email = form.getEmail();
        this.password = password;
        this.phone = form.getPhone();
        this.address = form.getAddress();
        this.roles = List.of(UserType.USER.getRole());
    }
}
