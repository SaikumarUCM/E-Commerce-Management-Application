package com.saikumar.microservice.orderservice.service;


import com.saikumar.microservice.orderservice.dto.OrderRequest;
import com.saikumar.microservice.orderservice.dto.OrderResponse;
import com.saikumar.microservice.orderservice.model.Order;
import com.saikumar.microservice.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.saikumar.microservice.orderservice.client.InventoryClient;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final InventoryClient inventoryClient;

    public OrderResponse placeOrder(OrderRequest orderRequest){

        var isProductInStock= inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if(isProductInStock){
            Order order= Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .skuCode(orderRequest.skuCode())
                    .quantity(orderRequest.quantity())
                    .price(orderRequest.price())
                    .build();
            Order savedOrder = orderRepository.save(order);
            log.info("Order Created successfully with ID");
            return new OrderResponse(
                    savedOrder.getId(),
                    savedOrder.getOrderNumber(),
                    savedOrder.getSkuCode(),
                    savedOrder.getPrice(),
                    savedOrder.getQuantity()
            );
        }else{
            throw new RuntimeException("Product with SkuCode: " + orderRequest.skuCode() + " is not in stock");
        }

    }



    public List<OrderResponse> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll()
                .stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getOrderNumber(),
                        order.getSkuCode(),
                        order.getPrice(),
                        order.getQuantity()))
                .toList();
    }
}
