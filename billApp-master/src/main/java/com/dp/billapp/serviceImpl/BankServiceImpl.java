package com.dp.billapp.serviceImpl;

import com.dp.billapp.daoService.BankDaoService;
import com.dp.billapp.model.BankDetails;
import com.dp.billapp.model.Showroom;
import com.dp.billapp.service.BankService;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class BankServiceImpl implements BankService {

    private BankDaoService bankDaoService;

    @Override
    public BankDetails save(BankDetails bankDetails) {

        return bankDaoService.saveBankDetails(bankDetails);
    }

    @Override
    public List<BankDetails> getAll() {
        return bankDaoService.getAllBankDetails();
    }

    @Override
    public Optional<BankDetails> getById(long id) {
        return bankDaoService.getBankDetailsById(id);
    }

    @Override
    public BankDetails update(BankDetails bankDetails) {
        return bankDaoService.updateBankDetails(bankDetails);
    }

    @Override
    public Boolean isAllDetailsPresent(String accNo,String ifsc) {
        return accNo=="" || ifsc=="";
    }

    @Override
    public String delete(long id) {
        bankDaoService.deleteBankDetails(id);
        return "Bank Details Deleted";
    }

    @Override
    public Optional<BankDetails> getBankByAccountNumber(String accountNumber) {
        return bankDaoService.getBankByAccountNumber(accountNumber);
    }
}

