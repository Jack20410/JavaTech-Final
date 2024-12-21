package com.tdtu.pos.repository;

import com.tdtu.pos.entity.Invoice;
import com.tdtu.pos.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Integer> {
    boolean findAllByInvoice(Invoice invoice);
    //List<InvoiceItem> findAllByInvoice(Invoice invoice);

}

