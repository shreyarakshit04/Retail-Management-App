package com.dp.billapp.daoService;

import com.dp.billapp.model.BankDetails;
import io.vavr.control.Option;

import java.util.List;
import java.util.Optional;


public interface BankDaoService {
    BankDetails saveBankDetails(BankDetails bankDetails);
    List<BankDetails> getAllBankDetails();
    Optional<BankDetails> getBankDetailsById(long id);
    BankDetails updateBankDetails(BankDetails bankDetails);
    String deleteBankDetails(long id);
    Optional<BankDetails> getBankByAccountNumber(String accountNumber);
}
