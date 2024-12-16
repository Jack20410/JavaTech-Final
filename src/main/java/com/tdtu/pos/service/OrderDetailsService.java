package com.tdtu.pos.service;

import com.tdtu.pos.entity.OrderDetails;
import com.tdtu.pos.repository.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailsService {
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public List<OrderDetails> getOrderDetailsByOrderId(int orderId) {
        return orderDetailsRepository.findByOrderId(orderId);
    }

    public void saveOrderDetails(OrderDetails orderDetails) {
        orderDetailsRepository.save(orderDetails);
    }
}

