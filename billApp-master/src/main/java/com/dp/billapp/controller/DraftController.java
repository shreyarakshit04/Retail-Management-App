package com.dp.billapp.controller;


import com.dp.billapp.model.*;
import com.dp.billapp.repository.BankRepository;
import com.dp.billapp.repository.DraftRepository;
import com.dp.billapp.repository.ShowroomRepository;
import com.dp.billapp.repository.UserRepository;
import com.dp.billapp.service.DraftService;
import com.dp.billapp.service.InvoiceService;
import com.dp.billapp.service.UserService;
import com.dp.billapp.serviceImpl.InvoiceServiceImpl;
import io.vavr.control.Option;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(value="/draft")
public class DraftController {

    @Autowired
    DraftService draftService;

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ShowroomRepository showroomRepository;
    @Autowired
    BankRepository bankRepository;
    @Autowired
    InvoiceServiceImpl invoiceServiceImpl;
    @Autowired
    DraftRepository draftRepository;


    @PostMapping("/save")
    public ResponseEntity<?> saveDraft(@RequestBody InvoiceRequest invoiceRequest, HttpServletRequest request){

        if(request.getContentLength()==0)
            return  new ResponseEntity<>("Token Not Found!!!", HttpStatus.NOT_FOUND);
        String userContact= userService.getContact(request);
        Option<User> userOptional = userService.findByContact(userContact);


        InvoiceResponse invoice = draftService.saveInvoice(invoiceRequest , userOptional.get());

        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/all")
    public ResponseEntity<?> allInvoice(){
        return ResponseEntity.ok(draftService.getAllInvoice());
    }

    @GetMapping("/search/draft/{invoiceId}")
    public ResponseEntity<?> findDraftById(@PathVariable String invoiceId){
        InvoiceResponse invoiceOptional =draftService.getDraftInvoiceById(invoiceId);
        if(invoiceOptional == null)
            return new ResponseEntity<>("Draft Not exists!", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(invoiceOptional);
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateDraft(@RequestBody UpdateDraftRequest updateDraftRequest, HttpServletRequest request){
        if(request.getContentLength()==0)
            return  new ResponseEntity<>("Token Not Found!!!",HttpStatus.NOT_FOUND);
        InvoiceResponse invoiceResponse = draftService.getDraftInvoiceById(updateDraftRequest.getInvoiceId());
        if(invoiceResponse == null)
            return new ResponseEntity<>("Invoice Not exists!", HttpStatus.NOT_FOUND);
        String userContact= userService.getContact(request);
        Option<User> employee = userService.findByContact(userContact);
        Option<User> customer = userRepository.findByContact(updateDraftRequest.getUserContact());

        Optional<Showroom> showroom = showroomRepository.findById(updateDraftRequest.getShowroomId());

        Optional<BankDetails> bank = bankRepository.findById(updateDraftRequest.getBankId());

        Date date = new Date();
        String strDateFormat = "dd/MM/yyyy/hhmmssa";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        String formattedDate = dateFormat.format(date);

        Draft draft = Draft.builder()
                .id(updateDraftRequest.getId())
                .invoiceId(invoiceResponse.getInvoiceId())
                .invoiceDate(invoiceResponse.getInvoiceDate())
                .showroomId(showroom.get().getId())
                .bankId(bank.get().getId())
                .customerId(customer.get().getId())
                .createdBy(invoiceResponse.getCreatedBy().getId())
                .createdAt(invoiceResponse.getCreatedAt())
                .updatedBy(employee.get().getId())
                .updatedAt(formattedDate)
                .paymentType(updateDraftRequest.getPaymentType())
                .isGst(updateDraftRequest.getIsGstEnabled())
                .sGst(updateDraftRequest.getIsGstEnabled().equals("1")?1.5:0.0)
                .cGst(updateDraftRequest.getIsGstEnabled().equals("1")?1.5:0.0)
                .invoiceDetails(updateDraftRequest.getInvoiceDetails())
                .totalAmount(invoiceServiceImpl.getTotalAmount(updateDraftRequest.getInvoiceDetails(),1.5, updateDraftRequest.getIsGstEnabled()))
                .build();

        return ResponseEntity.ok(draftService.updateDraft(draft));
    }
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteDraftById(@PathVariable long id) {
        draftRepository.deleteById(id);
        return ResponseEntity.ok("draft deleted!!!!!!!!!");
    }


    @Data
    public static class UpdateDraftRequest {
        private long id;
        String invoiceId;
        String invoiceDate;
        long showroomId;
        long bankId;
        String userContact;
        String paymentType;
        String isGstEnabled;
        List<InvoiceItem> invoiceDetails = new ArrayList<>();

    }

}
