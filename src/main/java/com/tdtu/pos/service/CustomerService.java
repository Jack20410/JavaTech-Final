package com.tdtu.pos.service;

import com.tdtu.pos.entity.Customer;
import com.tdtu.pos.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    // Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Save a new customer
//    public void saveCustomer(Customer customer) {
//        customerRepository.save(customer);
//    }

    public Customer saveCustomer(String name, String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }

        return customerRepository.findByPhoneNumber(phoneNumber)
                .orElseGet(() -> {
                    Customer customer = new Customer();
                    customer.setName(name);
                    customer.setPhoneNumber(phoneNumber);
                    return customerRepository.save(customer);
                });
    }

//    // Update an existing customer
//    public void updateCustomer(Customer updatedCustomer) {
//        Customer existingCustomer = customerRepository.findById(updatedCustomer.getId())
//                .orElseThrow(() -> new RuntimeException("Customer not found!"));
//        existingCustomer.setFullName(updatedCustomer.getFullName());
//        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
//        customerRepository.save(existingCustomer);
//    }
//
//    // Delete a customer by ID
//    public void deleteCustomer(int id) {
//        customerRepository.deleteById(id);
//    }


}
