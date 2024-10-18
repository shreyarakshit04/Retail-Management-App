package com.dp.billapp.service;

import com.dp.billapp.model.*;

import java.util.List;

public interface DraftService {

    InvoiceResponse saveInvoice(InvoiceRequest invoiceRequest, User user);
    List<InvoiceResponse> getAllInvoice();
    InvoiceResponse getDraftInvoiceById(String id);
    Draft updateDraft(Draft draft);
}
