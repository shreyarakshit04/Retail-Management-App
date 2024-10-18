package com.dp.billapp.controller;

import com.dp.billapp.model.BankDetails;

import com.dp.billapp.model.Showroom;
import com.dp.billapp.service.BankService;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value="/bank")
public class BankController {

private BankService bankService;

    @PostMapping("/save")
    public ResponseEntity<?> saveBankDetails(@RequestBody BankDetails bankDetails){
        Optional<BankDetails> detailsOptional= bankService.getBankByAccountNumber(bankDetails.getAccountNumber());

        if(detailsOptional.isPresent() && detailsOptional.get().getIsActive().equals("1"))
            return new ResponseEntity<>("Bank details already present", HttpStatus.BAD_REQUEST);

        if(bankService.isAllDetailsPresent(bankDetails.getAccountNumber(), bankDetails.getIfscCode()))
            return new ResponseEntity<>("Some details are not filled", HttpStatus.BAD_REQUEST);
        BankDetails details;
        if(detailsOptional.isPresent() && detailsOptional.get().getIsActive().equals("0")){

            detailsOptional.get().setIsActive("1");
           details = bankService.save(detailsOptional.get());
        }
        else{
            bankDetails.setIsActive("1");
            details = bankService.save(bankDetails);
        }

        return ResponseEntity.ok(details);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBankDetails(){
       List<BankDetails> allBankDetails = bankService.getAll();
       return ResponseEntity.ok(allBankDetails);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> getBankDetailsById(@PathVariable long id){
      Optional<BankDetails> bankDetailsOption = bankService.getById(id);
      if(bankDetailsOption.isEmpty())
          return new ResponseEntity<>("Bank Details not found",HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(bankDetailsOption);
    }

    @GetMapping("/search/account/{accountNumber}")
    public ResponseEntity<?> getBankDetailsById(@PathVariable String accountNumber){
        Optional<BankDetails> bankDetailsOption = bankService.getBankByAccountNumber(accountNumber);
        if(bankDetailsOption.isEmpty())
            return new ResponseEntity<>("Bank Details not found",HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(bankDetailsOption);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateBankDetails(@RequestBody BankDetails bankDetails){
        bankDetails.setIsActive("1");
        return ResponseEntity.ok(bankService.update(bankDetails));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteBankById(@PathVariable long id){
        Optional<BankDetails> bankDetailsOption = bankService.getById(id);
        if(bankDetailsOption.isEmpty())
            return new ResponseEntity<>("Bank not found,can't be deleted!!!!",HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(bankService.delete(id));
    }

}
