package com.dp.billapp.repository;

import com.dp.billapp.model.BankDetails;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<BankDetails,Long> {
    Optional<BankDetails> findByAccountNumber(String accountNumber);
    List<BankDetails> findAllByIsActive(String active);

}
