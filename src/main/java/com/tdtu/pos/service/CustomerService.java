package com.tdtu.pos.service;

import com.tdtu.pos.entity.Customer;
import com.tdtu.pos.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer getCustomerByPhone(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Customer not found with phone number: " + phoneNumber));
    }

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
