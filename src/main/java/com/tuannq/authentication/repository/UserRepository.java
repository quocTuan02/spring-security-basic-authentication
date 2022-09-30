package com.tuannq.authentication.repository;

import com.tuannq.authentication.entity.Users;
import com.tuannq.authentication.model.request.UserSearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    @Query("SELECT x1 FROM Users x1 WHERE x1.isDeleted = false AND LOWER(x1.email) LIKE LOWER(:email) ")
    Users findExistByEmail(@Param("email") String email);

    @Query("SELECT x1 FROM Users x1 WHERE x1.isDeleted = false AND LOWER(x1.username) LIKE LOWER(:username) ")
    Users findExistByUsername(@Param("username") String username);

    Users findExistByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

    default Users findExistByUsernameIgnoreCaseOrEmailIgnoreCase(String username){
        return findExistByUsernameIgnoreCaseOrEmailIgnoreCase(username, username);
    }

    Users findByPhone(String phone);

    Users findByEmail(String email);

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
            "and (:#{#form.address} is null or :#{#form.address} = '' or lower(coalesce(x1.address, '') ) like lower(concat('%',:#{#form.address},'%'))) " +
            "and (:#{#form.roles} is null or :#{#form.roles} = '' ) " +
            "and (:#{#form.id} is null or :#{#form.id} = '' or concat(x1.id, '') = :#{#form.id} ) ")
    Page<Users> search(UserSearchForm form, Pageable pageable);

}
