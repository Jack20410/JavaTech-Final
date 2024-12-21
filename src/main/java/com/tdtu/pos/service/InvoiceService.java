package com.tdtu.pos.service;

import com.tdtu.pos.entity.Customer;
import com.tdtu.pos.entity.Invoice;
import com.tdtu.pos.entity.InvoiceItem;
import com.tdtu.pos.repository.InvoiceItemRepository;
import com.tdtu.pos.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    public Invoice saveInvoice(Customer customer, BigDecimal totalPrice, String paymentMethod,
                               BigDecimal cashReceived, BigDecimal changeGiven, List<InvoiceItem> items) {
        // Create a new invoice
        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setTotalPrice(totalPrice);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setCashReceived(paymentMethod.equals("cash") ? cashReceived : null);
        invoice.setChangeGiven(paymentMethod.equals("cash") ? changeGiven : null);

        // Save the invoice
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Save invoice items
        items.forEach(item -> {
            item.setInvoice(savedInvoice);
            invoiceItemRepository.save(item);
        });
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        return savedInvoice;
    }

    public List<Invoice> getInvoicesByCustomer(Customer customer) {
        return invoiceRepository.findByCustomer(customer);
    }
}

