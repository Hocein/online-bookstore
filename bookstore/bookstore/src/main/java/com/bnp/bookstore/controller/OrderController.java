package com.bnp.bookstore.controller;

import com.bnp.bookstore.entity.Order;
import com.bnp.bookstore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Checkout method that retrieves the userId from the JWT token
    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = (Long) authentication.getDetails(); // Extract userId from Authentication details
            Order savedOrder = orderService.checkout(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Get all orders for the authenticated user
    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = (Long) authentication.getDetails(); // Extract userId from Authentication details
            List<Order> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(orders);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
