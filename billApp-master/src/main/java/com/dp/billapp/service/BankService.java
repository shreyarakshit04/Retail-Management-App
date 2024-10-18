package com.dp.billapp.service;

import com.dp.billapp.model.BankDetails;
import io.vavr.control.Option;

import java.util.List;
import java.util.Optional;

public interface BankService {

    BankDetails save(BankDetails bankDetails);
    List<BankDetails> getAll();
    Optional<BankDetails> getById(long id);
    BankDetails update(BankDetails bankDetails);
    Boolean isAllDetailsPresent(String accNo,String ifsc);
    String delete(long id);
    Optional<BankDetails> getBankByAccountNumber(String accountNumber);

}
