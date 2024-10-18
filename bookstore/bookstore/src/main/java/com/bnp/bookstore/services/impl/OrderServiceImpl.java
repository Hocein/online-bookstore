package com.bnp.bookstore.services.impl;

import com.bnp.bookstore.entity.CartItem;
import com.bnp.bookstore.entity.Order;
import com.bnp.bookstore.repository.CartItemRepository;
import com.bnp.bookstore.repository.OrderRepository;
import com.bnp.bookstore.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Order checkout(Long userId) {
        // Find all cart items associated with the user ID
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            logger.warn("Attempted checkout with empty cart by user ID: {}", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }

        // Calculate the total amount for the order
        double total = cartItems.stream()
                .mapToDouble(item -> item.getBook().getPrice() * item.getQuantity())
                .sum();

        // Create and save the order
        Order order = new Order();
        order.setUserId(userId);  // Set user ID directly on the order
        order.setCartItems(cartItems);
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);

        // Clear the user's cart after order completion
        cartItemRepository.deleteAll(cartItems);

        logger.info("Order created for user ID {} with total: {}", userId, total);
        return savedOrder;
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId); // Fetch orders directly by user ID
    }
}
