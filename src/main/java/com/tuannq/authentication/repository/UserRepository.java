package com.tuannq.authentication.repository;

import com.tuannq.authentication.entity.Users;
import com.tuannq.authentication.model.request.UserSearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    @Query("SELECT x1 FROM Users x1 WHERE x1.isDeleted = false AND LOWER(x1.email) LIKE LOWER(:email) ")
    Users findExistByEmail(@Param("email") String email);

    Users findByUsername(String username);

    Users findExistByUsernameOrEmailIgnoreCase(String username, String email);

    default Users findExistByUsernameOrEmailIgnoreCase(String username) {
        return findExistByUsernameOrEmailIgnoreCase(username, username);
    }

    Users findByPhone(String phone);

    Users findByEmailIgnoreCase(String email);

    @Query("select x1 from Users x1 " +
            "where :keyword is null " +
            "or lower(x1.fullName) like lower(concat('%',:keyword,'%')) " +
            "or lower(x1.email) like lower(concat('%',:keyword,'%')) " +
            "or lower(x1.address) like lower(concat('%',:keyword,'%')) " +
            "or lower(x1.phone) like lower(concat('%',:keyword,'%')) ")
    Page<Users> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("select x1 from Users x1 " +
            "where (:#{#form.fullName} is null or :#{#form.fullName} = '' or lower(coalesce(x1.fullName, '') ) like lower(concat('%',:#{#form.fullName},'%'))) " +
            "and (:#{#form.email} is null or :#{#form.email} = '' or lower(coalesce(x1.email, '') ) like lower(concat('%',:#{#form.email},'%'))) " +
            "and (:#{#form.phone} is null or :#{#form.phone} = '' or lower(coalesce(x1.phone, '') ) like lower(concat('%',:#{#form.phone},'%'))) " +
            "and (:#{#form.status} is null or :#{#form.status} = '' or lower(coalesce(x1.status, '') ) like lower(concat('%',:#{#form.status},'%'))) " +
            "and (:#{#form.address} is null or :#{#form.address} = '' or lower(coalesce(x1.address, '') ) like lower(concat('%',:#{#form.address},'%'))) " +
            "and (:#{#form.role} is null or :#{#form.role} = '' or lower(coalesce(x1.role, '') ) = lower(:#{#form.role})) " +
            "and (:#{#form.id} is null or :#{#form.id} = '' or concat(x1.id, '') = :#{#form.id} ) " +
            "and ('admin' = lower(:#{#role}) or ('employee' = lower(:#{#role}) and lower(coalesce(x1.role, '')) = 'user'))")
    Page<Users> search(String role, UserSearchForm form, Pageable pageable);

}
