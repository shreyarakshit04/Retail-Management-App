package com.dp.billapp.daoServiceImpl;

import com.dp.billapp.daoService.BankDaoService;
import com.dp.billapp.model.BankDetails;
import com.dp.billapp.repository.BankRepository;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BankDaoServiceImpl implements BankDaoService {

    private BankRepository bankRepository;
    @Override
    public BankDetails saveBankDetails(BankDetails bankDetails) {
        return bankRepository.save(bankDetails);
    }

    @Override
    public List<BankDetails> getAllBankDetails() {

        return bankRepository.findAllByIsActive("1");
    }

    @Override
    public Optional<BankDetails> getBankDetailsById(long id) {
        return bankRepository.findById(id);
    }

    @Override
    public BankDetails updateBankDetails(BankDetails bankDetails) {
        return bankRepository.save(bankDetails);
    }

    @Override
    public String deleteBankDetails(long id) {
      Optional<BankDetails> bank = bankRepository.findById(id);
      bank.get().setIsActive("0");
      bankRepository.save(bank.get());

        return "Bank deactivated !!!";
    }

    @Override
    public Optional<BankDetails> getBankByAccountNumber(String accountNumber) {
        return bankRepository.findByAccountNumber(accountNumber);
    }


}
