package com.tdtu.pos.repository;

import com.tdtu.pos.entity.Customer;
import com.tdtu.pos.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByCustomer(Customer customer);
}

