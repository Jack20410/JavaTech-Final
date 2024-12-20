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
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    // Update an existing customer
    public void updateCustomer(Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(updatedCustomer.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found!"));
        existingCustomer.setFullName(updatedCustomer.getFullName());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        existingCustomer.setAddress(updatedCustomer.getAddress());
        existingCustomer.setActive(updatedCustomer.getActive());
        customerRepository.save(existingCustomer);
    }

    // Delete a customer by ID
    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }

    // Toggle active status
    public void toggleActiveStatus(int id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found!"));
        customer.setActive(!customer.getActive());
        customerRepository.save(customer);
    }
}
