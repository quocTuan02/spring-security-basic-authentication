package com.tuannq.authentication.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String transaction;
    @Column(nullable = false)
    private String otp;
    private String numberPhone;
    private String email;
    @Column(nullable = false)
    private Instant expiration;
    private Integer totalRequest = 0;
    @Column(nullable = false, columnDefinition = "BOOLEAN default false")
    private Boolean isDeleted = false;
    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by")
    private Users createdBy;
    @CreatedDate
    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    public OTP(
            String transaction,
            String otp,
            String numberPhone,
            String email,
            Instant expiration
    ) {
        this.transaction = transaction;
        this.otp = otp;
        this.numberPhone = numberPhone;
        this.email = email;
        this.expiration = expiration;
    }
}
