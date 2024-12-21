package com.tdtu.pos.controller;

import com.tdtu.pos.DTO.PurchaseRequest;
import com.tdtu.pos.entity.Customer;
import com.tdtu.pos.entity.Invoice;
import com.tdtu.pos.entity.InvoiceItem;
import com.tdtu.pos.entity.Product;
import com.tdtu.pos.service.CustomerService;
import com.tdtu.pos.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private static final Logger log = LoggerFactory.getLogger(SalesController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/purchase")
    public ResponseEntity<?> savePurchase(@RequestBody PurchaseRequest request) {
        log.info("Received purchase request: {}", request);

        if (request.getCustomerName() == null || request.getCustomerPhone() == null) {
            return ResponseEntity.badRequest().body("Customer details are required");
        }

        Customer customer = customerService.saveCustomer(request.getCustomerName(), request.getCustomerPhone());
        log.info("Customer saved/retrieved: {}", customer);

        List<InvoiceItem> items = request.getItems().stream().map(item -> {
            log.info("Processing item: {}", item);
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setProduct(new Product(Math.toIntExact(item.getProductId())));
            invoiceItem.setQuantity(item.getQuantity());
            invoiceItem.setPrice(item.getPrice());
            invoiceItem.setTotal(item.getTotal());
            return invoiceItem;
        }).collect(Collectors.toList());

        if (items.isEmpty()) {
            return ResponseEntity.badRequest().body("Invoice must have at least one item");
        }

        Invoice invoice = invoiceService.saveInvoice(
                customer,
                request.getTotalPrice(),
                request.getPaymentMethod(),
                request.getCashReceived(),
                request.getChangeGiven(),
                items
        );
        log.info("Invoice saved: {}", invoice);

        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/history/{phoneNumber}")
    public ResponseEntity<?> getPurchaseHistory(@PathVariable String phoneNumber) {
        Customer customer = customerService.saveCustomer(null, phoneNumber);
        List<Invoice> invoices = invoiceService.getInvoicesByCustomer(customer);
        return ResponseEntity.ok(invoices);
    }
}

