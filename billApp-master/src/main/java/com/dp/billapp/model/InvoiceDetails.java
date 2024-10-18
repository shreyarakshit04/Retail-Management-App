package com.dp.billapp.model;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceDetails {

    List<InvoiceItem> invoiceItemList;
}
