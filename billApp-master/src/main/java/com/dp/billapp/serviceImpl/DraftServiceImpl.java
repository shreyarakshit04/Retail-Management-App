package com.dp.billapp.serviceImpl;


import com.dp.billapp.daoService.DraftDaoService;
import com.dp.billapp.daoService.InvoiceDaoService;
import com.dp.billapp.daoServiceImpl.DraftDaoServiceImpl;
import com.dp.billapp.model.*;
import com.dp.billapp.repository.BankRepository;
import com.dp.billapp.repository.ShowroomRepository;
import com.dp.billapp.repository.UserRepository;
import com.dp.billapp.service.DraftService;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class DraftServiceImpl  implements DraftService {

    @Autowired
    private DraftDaoService draftDaoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowroomRepository showroomRepository;

    @Autowired
    private BankRepository bankRepository;

    @Override
    public InvoiceResponse saveInvoice(InvoiceRequest invoiceRequest, User employee) {

        final double gst = 1.5;

        Option<User> user = userRepository.findByContact(invoiceRequest.getUserContact());

        Optional<Showroom> showroom = showroomRepository.findById(invoiceRequest.getShowroomId());

        Optional<BankDetails> bank = bankRepository.findById(invoiceRequest.getBankId());

        log.info("#  invoice request - {}", invoiceRequest);

        Date date = new Date();
        String strDateFormat = "dd/MM/yyyy/hhmmssa";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        String formattedDate = dateFormat.format(date);

        Draft draft = new Draft();
        draft.setCreatedBy(employee.getId());
        draft.setUpdatedBy(employee.getId());
        draft.setCreatedAt(formattedDate);
        draft.setUpdatedAt(formattedDate);
        draft.setInvoiceDetails(invoiceRequest.getInvoiceDetails());
        draft.setInvoiceId(generateInvoiceId(invoiceRequest.getInvoiceDate()));
        draft.setInvoiceDate(invoiceRequest.getInvoiceDate());
        draft.setShowroomId(invoiceRequest.getShowroomId());
        draft.setBankId(invoiceRequest.getBankId());
        draft.setCustomerId(user.get().getId());
        draft.setPaymentType(invoiceRequest.getPaymentType());
        draft.setIsGst(invoiceRequest.getIsGstEnabled());

        if(draft.getIsGst().equals("0")){
            draft.setCGst(0);
            draft.setSGst(0);
        }else{
            draft.setCGst(gst);
            draft.setSGst(gst);
        }

        if(invoiceRequest.getInvoiceDetails()!=null)
            draft.setTotalAmount(getTotalAmount(invoiceRequest.getInvoiceDetails(),gst, invoiceRequest.getIsGstEnabled()));
        else
            draft.setTotalAmount("0");

        Draft response = draftDaoService.saveInvoice(draft);

        InvoiceResponse invoiceResponse = InvoiceResponse.builder()
                .id(response.getId())
                .invoiceId(response.getInvoiceId())
                .invoiceDate(response.getInvoiceDate())
                .showroom(showroom.get())
                .bankDetails(bank.get())
                .customer(user.get())
                .updatedBy(userRepository.findById(employee.getId()).get())
                .createdBy(userRepository.findById(employee.getId()).get())
                .updatedAt(formattedDate)
                .createdAt(formattedDate)
                .paymentType(response.getPaymentType())
                .isGst(response.getIsGst())
                .cGst(response.getCGst())
                .sGst(response.getSGst())
                .totalAmount(response.getTotalAmount())
                .invoiceDetails(response.getInvoiceDetails())
                .build();

        return invoiceResponse;
    }

    @Override
    public List<InvoiceResponse> getAllInvoice() {

        List<Draft> invoiceList =  draftDaoService.getAllInvoice();
        List<InvoiceResponse> invoiceResponseList = new ArrayList<>();
        for(Draft response:invoiceList){
            InvoiceResponse invoiceResponse = InvoiceResponse.builder()
                    .id(response.getId())
                    .invoiceId(response.getInvoiceId())
                    .invoiceDate(response.getInvoiceDate())
                    .showroom(showroomRepository.findById(response.getShowroomId()).get())
                    .bankDetails(bankRepository.findById(response.getBankId()).get())
                    .customer(userRepository.findById(response.getCustomerId()).get())
                    .updatedBy(userRepository.findById(response.getUpdatedBy()).get())
                    .createdBy(userRepository.findById(response.getCreatedBy()).get())
                    .updatedAt(response.getCreatedAt())
                    .createdAt(response.getUpdatedAt())
                    .paymentType(response.getPaymentType())
                    .isGst(response.getIsGst())
                    .cGst(response.getCGst())
                    .sGst(response.getSGst())
                    .totalAmount(response.getTotalAmount())
                    .invoiceDetails(response.getInvoiceDetails())
                    .build();
            invoiceResponseList.add(invoiceResponse);
        }
        return invoiceResponseList;
    }


    @Override
    public InvoiceResponse getDraftInvoiceById(String id) {
        Optional<Draft> result = draftDaoService.getDraftInvoiceById(id);
        Draft response = result.get();
        InvoiceResponse invoiceResponse = InvoiceResponse.builder()
                .id(response.getId())
                .invoiceId(response.getInvoiceId())
                .invoiceDate(response.getInvoiceDate())
                .showroom(showroomRepository.findById(response.getShowroomId()).get())
                .bankDetails(bankRepository.findById(response.getBankId()).get())
                .customer(userRepository.findById(response.getCustomerId()).get())
                .updatedBy(userRepository.findById(response.getUpdatedBy()).get())
                .createdBy(userRepository.findById(response.getCreatedBy()).get())
                .updatedAt(response.getCreatedAt())
                .createdAt(response.getUpdatedAt())
                .paymentType(response.getPaymentType())
                .isGst(response.getIsGst())
                .cGst(response.getCGst())
                .sGst(response.getSGst())
                .totalAmount(response.getTotalAmount())
                .invoiceDetails(response.getInvoiceDetails())
                .build();

        return invoiceResponse;
    }

    @Override
    public Draft updateDraft(Draft draft) {
        return draftDaoService.updateDraft(draft);
    }

    public String getTotalAmount(List<InvoiceItem> invoiceDetails, double gst, String isGstEnabled) {
        double allItemAmount = 0 ;
        for(InvoiceItem invoiceItem:invoiceDetails){
            if (!invoiceItem.getAmount().equals(""))
                allItemAmount+=Double.parseDouble(invoiceItem.getAmount());
            else
                allItemAmount+=0;

        }
        double afterGstCalculation = 0;
        if(isGstEnabled.equals("1")){
            double gstAmount = (allItemAmount*gst)/100;
            afterGstCalculation = gstAmount * 2;
        }
        double finalAmount = allItemAmount + afterGstCalculation;
        return Double.toString(finalAmount);
    }


    private String generateInvoiceId(String invoiceDate){
        Date date = new Date();
        String strDateFormat = "hhmmss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        String formattedDate = dateFormat.format(date);
        String invoiceId = invoiceDate+"_"+formattedDate;
        return invoiceId;
    }
}
