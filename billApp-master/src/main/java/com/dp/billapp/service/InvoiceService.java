package com.dp.billapp.service;

import com.dp.billapp.model.Invoice;
import com.dp.billapp.model.InvoiceRequest;
import com.dp.billapp.model.InvoiceResponse;
import com.dp.billapp.model.User;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {

    InvoiceResponse saveInvoice(InvoiceRequest invoiceRequest, User user);
    List<InvoiceResponse> getAllInvoice();
    InvoiceResponse getInvoiceById(long id);
    Optional<Invoice> getInvoiceByInvoiceId(String invoiceId);
    Invoice updateInvoice(Invoice invoice);
    String deleteInvoice(long id);

}
