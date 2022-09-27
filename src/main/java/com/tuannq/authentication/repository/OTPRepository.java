package com.tuannq.authentication.repository;

import com.tuannq.authentication.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    @Query(nativeQuery = true,
            value = " select * " +
                    " from otp x1" +
                    "          join (" +
                    "     select x2.transaction     as x2_transaction," +
                    "            x2.email           as x2_email," +
                    "            max(x2.expiration) as x2_expiration" +
                    "     from otp x2" +
                    "     where x2.transaction = :transaction" +
                    "       and x2.email = :email" +
                    "     group by x2.transaction, x2.email" +
                    " ) as x3 on x1.email = x3.x2_email" +
                    "     and x1.transaction = x3.x2_transaction" +
                    "     and x1.expiration = x3.x2_expiration" +
                    "     and x1.is_deleted = 0")
    Optional<OTP> findTransactionAndEmail(
            @Param("transaction") String Transaction,
            @Param("email") String email
    );
}
