package com.tuannq.authentication.model.dto;

import com.tuannq.authentication.entity.Users;
import com.tuannq.authentication.util.ConverterUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AbstractDTO {
    private Long id;
    private DataUser createdBy;
    private String createdAt;
    private DataUser modifiedBy;
    private String modifiedAt;
    private Boolean isDeleted;

    public AbstractDTO(
            Long id,
            Users createdBy,
            Date createdAt,
            Users modifiedBy,
            Date modifiedAt,
            Boolean isDeleted
    ) {
        this.id = id;
        if (createdBy != null)
            this.createdBy = new DataUser(createdBy);
        this.createdAt = ConverterUtils.formatDateToDatetimeString(createdAt);
        if (modifiedBy != null)
            this.modifiedBy = new DataUser(modifiedBy);
        this.modifiedAt = ConverterUtils.formatDateToDatetimeString(modifiedAt);
        this.isDeleted = isDeleted;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class DataUser {
        private Long id;
        private String fullName;

        public DataUser(Users user) {
            this.id = user.getId();
            this.fullName = user.getFullName();
        }
    }
}
