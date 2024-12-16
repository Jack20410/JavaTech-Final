package com.tdtu.pos.service;

import com.tdtu.pos.entity.Customer;
import com.tdtu.pos.entity.Order;
import com.tdtu.pos.entity.Product;
import com.tdtu.pos.repository.CustomerRepository;
import com.tdtu.pos.repository.OrderRepository;
import com.tdtu.pos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalespersonService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Customer getCustomerByPhone(String phone) {
        return customerRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Order processOrder(Order order) {
        if (order.getDetails() == null || order.getDetails().isEmpty()) {
            throw new IllegalArgumentException("Order must contain details.");
        }

        double totalAmount = order.getDetails()
                .stream()
                .mapToDouble(d -> d.getUnitPrice() * d.getQuantity())
                .sum();
        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }
}

