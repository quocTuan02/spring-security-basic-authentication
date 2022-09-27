package com.tuannq.authentication.entity;

import com.tuannq.authentication.entity.core.BaseEntity;
import com.tuannq.authentication.model.request.UserForm;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

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

    public Users(UserForm form, String password) {
        this.setUser(form, password);
    }

    public void setUser(UserForm form, String password) {
        this.username = form.getUsername();
        this.fullName = form.getFullName();
        this.email = form.getEmail();
        this.phone = form.getPhone();
        this.address = form.getAddress();
        this.password = password;
    }
}
