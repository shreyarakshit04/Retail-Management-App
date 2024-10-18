package com.dp.billapp.daoService;

import com.dp.billapp.model.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceDaoService {

    Invoice saveInvoice(Invoice invoice);
    List<Invoice> getAllInvoice();
    Optional<Invoice> getInvoiceById(long id);
    Optional<Invoice> getInvoiceByInvoiceId(String invoiceId);
    Invoice updateInvoice(Invoice invoice);
    String deleteInvoice(long id);

}
