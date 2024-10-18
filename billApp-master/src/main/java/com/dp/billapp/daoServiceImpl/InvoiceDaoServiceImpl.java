package com.dp.billapp.daoServiceImpl;

import com.dp.billapp.daoService.InvoiceDaoService;
import com.dp.billapp.model.Invoice;
import com.dp.billapp.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceDaoServiceImpl implements InvoiceDaoService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public List<Invoice> getAllInvoice() {
        return invoiceRepository.findAll();
    }

    @Override
    public Optional<Invoice> getInvoiceById(long id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public Optional<Invoice> getInvoiceByInvoiceId(String invoiceId) {
        return invoiceRepository.findByInvoiceId(invoiceId);
    }

    @Override
    public Invoice updateInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public String deleteInvoice(long id) {
        invoiceRepository.deleteById(id);
        return "Invoice deleted";
    }
}
