package com.tdtu.pos.repository;

import com.tdtu.pos.entity.Customer;
import com.tdtu.pos.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    List<Invoice> findByCustomer(Customer customer);
    @Query("SELECT COALESCE(SUM(i.totalPrice), 0) FROM Invoice i")
    BigDecimal findTotalSales();

}


