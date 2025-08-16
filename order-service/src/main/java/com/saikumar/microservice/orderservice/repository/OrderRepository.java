package com.saikumar.microservice.orderservice.repository;

import com.saikumar.microservice.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
