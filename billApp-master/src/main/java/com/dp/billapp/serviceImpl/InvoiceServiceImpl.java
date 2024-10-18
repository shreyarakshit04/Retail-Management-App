package com.dp.billapp.serviceImpl;

import com.dp.billapp.daoService.InvoiceDaoService;
import com.dp.billapp.model.*;
import com.dp.billapp.repository.BankRepository;
import com.dp.billapp.repository.DraftRepository;
import com.dp.billapp.repository.ShowroomRepository;
import com.dp.billapp.repository.UserRepository;
import com.dp.billapp.service.InvoiceService;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceDaoService invoiceDaoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowroomRepository showroomRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private DraftRepository draftRepository;

    @Override
    public InvoiceResponse saveInvoice(InvoiceRequest invoiceRequest , User employee) {

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

        Invoice invoice = new Invoice();
        invoice.setCreatedBy(employee.getId());
        invoice.setUpdatedBy(employee.getId());
        invoice.setCreatedAt(formattedDate);
        invoice.setUpdatedAt(formattedDate);
        invoice.setInvoiceDetails(invoiceRequest.getInvoiceDetails());
        invoice.setInvoiceId(generateInvoiceId(invoiceRequest.getInvoiceDate()));
        invoice.setInvoiceDate(invoiceRequest.getInvoiceDate());
        invoice.setShowroomId(showroom.get().getId());
        invoice.setBankId(bank.get().getId());
        invoice.setCustomerId(user.get().getId());
        invoice.setPaymentType(invoiceRequest.getPaymentType());
        invoice.setIsGst(invoiceRequest.getIsGstEnabled());

        if(invoice.getIsGst().equals("0")){
            invoice.setCGst(0);
            invoice.setSGst(0);
        }else{
            invoice.setCGst(gst);
            invoice.setSGst(gst);
        }

        invoice.setTotalAmount(getTotalAmount(invoiceRequest.getInvoiceDetails(),gst, invoiceRequest.getIsGstEnabled()));


        if(draftRepository.findByInvoiceId(invoiceRequest.getInvoiceId()).isPresent()){
            Optional<Draft> draftResponse = draftRepository.findByInvoiceId(invoiceRequest.getInvoiceId());
            draftRepository.deleteById(draftResponse.get().getId());
        }
        Invoice response = invoiceDaoService.saveInvoice(invoice);

        InvoiceResponse invoiceResponse = InvoiceResponse.builder()
                .id(response.getId())
                .invoiceId(response.getInvoiceId())
                .invoiceDate(response.getInvoiceDate())
                .showroom(showroom.get())
                .bankDetails(bank.get())
                .customer(user.get())
                .updatedBy(employee)
                .createdBy(employee)
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
        List<Invoice> invoiceList =  invoiceDaoService.getAllInvoice();
        List<InvoiceResponse> invoiceResponseList = new ArrayList<>();
        for(Invoice response:invoiceList){
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
    public InvoiceResponse getInvoiceById(long id) {
        Optional<Invoice> result = invoiceDaoService.getInvoiceById(id);
        Invoice response = result.get();
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
    public Optional<Invoice> getInvoiceByInvoiceId(String invoiceId) {
        return invoiceDaoService.getInvoiceByInvoiceId(invoiceId);
    }

    @Override
    public Invoice updateInvoice(Invoice invoice) {
        return invoiceDaoService.updateInvoice(invoice);
    }

    @Override
    public String deleteInvoice(long id) {
        invoiceDaoService.deleteInvoice(id);
        return "Invoice deleted";
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
