package com.tdtu.pos.repository;

import com.tdtu.pos.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    @Query("SELECT c FROM Customer c")
    List<Customer> findAll();

}

