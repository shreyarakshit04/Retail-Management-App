package com.dp.billapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InvoiceResponse {

    private long id;

    private String invoiceId;

    private String invoiceDate;

    private Showroom showroom;

    private BankDetails bankDetails;

    private User customer;

    private User updatedBy;

    private User createdBy;

    private String createdAt;

    private String updatedAt;

    private String paymentType;

    private String isGst;

    private double sGst;

    private double cGst;

    private String totalAmount;

    List<InvoiceItem> invoiceDetails;

}
