package com.tuannq.authentication.entity.core;

import com.tuannq.authentication.entity.Users;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by")
    private Users createdBy;
    @CreatedDate
    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;
    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "modified_by")
    private Users modifiedBy;
    @LastModifiedDate
    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date modifiedAt;
    @Column(nullable = false, columnDefinition = "BOOLEAN default false")
    private Boolean isDeleted = false;

}
