package com.tuannq.authentication.entity;

import com.tuannq.authentication.entity.core.BaseEntity;
import com.tuannq.authentication.model.request.UpdateProfileForm;
import com.tuannq.authentication.model.request.UserFormAdmin;
import com.tuannq.authentication.model.request.UserFormCustomer;
import com.tuannq.authentication.model.type.UserType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

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
    @Column(nullable = false, unique = true, length = 511)
    private String username;
    @Column(nullable = false, length = 511)
    private String fullName;
    @Column(nullable = false, unique = true, length = 511)
    private String email;
    @Column(nullable = false, length = 511)
    private String password;
    @Column(length = 511)
    private String phone;
    @Column(length = 511)
    private String address;

    @Column(nullable = false, length = 127)
    private String role;

    @Column(length = 127)
    public String employee_isActive;
    @Column(length = 2047)
    public String employee_note;
    @Column(length = 127)
    public String admin_isActive;
    @Column(length = 2047)
    public String admin_note;


    public Users(String username, String fullName, String email, String password, String phone, String address, String role) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public void setUser(UserFormAdmin form) {
        this.fullName = form.getFullName();
        this.email = form.getEmail();
        this.phone = form.getPhone();
        this.address = form.getAddress();
        this.role = form.getRole();
        this.username = form.getUsername();
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
        this.role = form.getRole();
        this.username = form.getUsername();
    }

    public Users(UserFormCustomer form, String password) {
        this.fullName = form.getFullName();
        this.email = form.getEmail();
        this.password = password;
        this.phone = form.getPhone();
        this.address = form.getAddress();
        this.role = UserType.USER.getRole();
        this.username = form.getUsername();
    }
}
