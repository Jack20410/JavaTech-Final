package com.tdtu.pos.repository;

import com.tdtu.pos.entity.OrderDetails;
import com.tdtu.pos.entity.OrderDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, OrderDetailsId> {
    List<OrderDetails> findByOrderId(int orderId);
}
