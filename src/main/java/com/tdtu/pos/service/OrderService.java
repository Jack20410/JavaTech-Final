package com.tdtu.pos.service;

import com.tdtu.pos.entity.Order;
import com.tdtu.pos.entity.OrderDetails;
import com.tdtu.pos.repository.OrderDetailsRepository;
import com.tdtu.pos.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }

    public Order processOrder(Order order) {
        if (order.getCustomer() == null) {
            throw new IllegalArgumentException("Order must have a customer.");
        }

        Order savedOrder = orderRepository.save(order);

        if (order.getDetails() != null && !order.getDetails().isEmpty()) {
            for (OrderDetails details : order.getDetails()) {
                details.setOrder(savedOrder);
                orderDetailsRepository.save(details);
            }
        }

        return savedOrder;
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
