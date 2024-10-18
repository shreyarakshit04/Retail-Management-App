package com.dp.billapp.controller;

import com.dp.billapp.model.*;
import com.dp.billapp.repository.BankRepository;
import com.dp.billapp.repository.ShowroomRepository;
import com.dp.billapp.repository.UserRepository;
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

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(value="/invoice")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

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

    @PostMapping("/save")
    public ResponseEntity<?> saveInvoice(@RequestBody InvoiceRequest invoiceRequest, HttpServletRequest request){

        if(request.getContentLength()==0)
            return  new ResponseEntity<>("Token Not Found!!!",HttpStatus.NOT_FOUND);
        String userContact= userService.getContact(request);
        Option<User> userOptional = userService.findByContact(userContact);


        InvoiceResponse invoice = invoiceService.saveInvoice(invoiceRequest , userOptional.get());

        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/all")
    public ResponseEntity<?> allInvoice(){
        return ResponseEntity.ok(invoiceService.getAllInvoice());
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> findInvoiceById(@PathVariable long id){
        InvoiceResponse invoiceResponse =invoiceService.getInvoiceById(id);
        if(invoiceResponse==null)
            return new ResponseEntity<>("Invoice Not exists!", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(invoiceResponse);
    }
    @GetMapping("/search/invoice/{invoiceId}")
    public ResponseEntity<?> findInvoiceById(@PathVariable String invoiceId){
        Optional<Invoice> invoiceOptional =invoiceService.getInvoiceByInvoiceId(invoiceId);
        if(invoiceOptional.isEmpty())
            return new ResponseEntity<>("Invoice Not exists!", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(invoiceOptional);
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateInvoice(@RequestBody UpdateInvoiceRequest updateInvoiceRequest,HttpServletRequest request){
        if(request.getContentLength()==0)
            return  new ResponseEntity<>("Token Not Found!!!",HttpStatus.NOT_FOUND);
        String userContact= userService.getContact(request);
        Option<User> employee = userService.findByContact(userContact);
        Option<User> customer = userRepository.findByContact(updateInvoiceRequest.getUserContact());

        Optional<Showroom> showroom = showroomRepository.findById(updateInvoiceRequest.getShowroomId());

        Optional<BankDetails> bank = bankRepository.findById(updateInvoiceRequest.getBankId());

        Date date = new Date();
        String strDateFormat = "dd/MM/yyyy/hhmmssa";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        String formattedDate = dateFormat.format(date);

        InvoiceResponse invoiceResponse = invoiceService.getInvoiceById(updateInvoiceRequest.getId());
        if(invoiceResponse == null)
            return new ResponseEntity<>("Invoice Not exists!", HttpStatus.NOT_FOUND);

        Invoice invoice = Invoice.builder()
                .id(updateInvoiceRequest.getId())
                .invoiceId(invoiceResponse.getInvoiceId())
                .invoiceDate(invoiceResponse.getInvoiceDate())
                .showroomId(showroom.get().getId())
                .bankId(bank.get().getId())
                .customerId(customer.get().getId())
                .createdBy(invoiceResponse.getCreatedBy().getId())
                .createdAt(invoiceResponse.getCreatedAt())
                .updatedBy(employee.get().getId())
                .updatedAt(formattedDate)
                .paymentType(updateInvoiceRequest.getPaymentType())
                .isGst(updateInvoiceRequest.getIsGstEnabled())
                .sGst(updateInvoiceRequest.getIsGstEnabled().equals("1")?1.5:0.0)
                .cGst(updateInvoiceRequest.getIsGstEnabled().equals("1")?1.5:0.0)
                .invoiceDetails(updateInvoiceRequest.getInvoiceDetails())
                .totalAmount(invoiceServiceImpl.getTotalAmount(updateInvoiceRequest.getInvoiceDetails(),1.5, updateInvoiceRequest.getIsGstEnabled()))
                .build();



        return ResponseEntity.ok(invoiceService.updateInvoice(invoice));
    }
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable long id){
        InvoiceResponse invoiceResponse =invoiceService.getInvoiceById(id);
        if(invoiceResponse == null)
            return new ResponseEntity<>("Invoice doesn't exist,can't be deleted",HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(invoiceService.deleteInvoice(id));
    }
@Data
    public static class UpdateInvoiceRequest {
        private long id;

        String invoiceDate;
        long showroomId;
        long bankId;
        String userContact;
        String paymentType;
        String isGstEnabled;
        List<InvoiceItem> invoiceDetails = new ArrayList<>();

    }


}
