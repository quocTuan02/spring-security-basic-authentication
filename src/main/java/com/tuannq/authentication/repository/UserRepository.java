package com.tuannq.authentication.repository;

import com.tuannq.authentication.entity.Users;
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

    Users findByPhone(String phone);

    Users findByEmail(String email);

    @Query("select x1 from Users x1 " +
            "where :keyword is null " +
            "or lower(x1.fullName) like lower(concat('%',:keyword,'%')) " +
            "or lower(x1.email) like lower(concat('%',:keyword,'%')) " +
            "or lower(x1.address) like lower(concat('%',:keyword,'%')) " +
            "or lower(x1.phone) like lower(concat('%',:keyword,'%')) ")
    Page<Users> search(@Param("keyword") String keyword, Pageable pageable);

}
