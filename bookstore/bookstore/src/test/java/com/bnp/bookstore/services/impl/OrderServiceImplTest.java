package com.bnp.bookstore.services.impl;

import com.bnp.bookstore.entity.CartItem;
import com.bnp.bookstore.entity.Order;
import com.bnp.bookstore.entity.Book;
import com.bnp.bookstore.repository.CartItemRepository;
import com.bnp.bookstore.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckout_Success() {
        // Arrange
        Long userId = 1L;

        Book book = new Book();
        book.setPrice(20.0);

        CartItem cartItem1 = new CartItem();
        cartItem1.setBook(book);
        cartItem1.setQuantity(2);

        CartItem cartItem2 = new CartItem();
        cartItem2.setBook(book);
        cartItem2.setQuantity(1);

        List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2);
        when(cartItemRepository.findByUserId(userId)).thenReturn(cartItems);

        Order savedOrder = new Order();
        savedOrder.setUserId(userId);
        savedOrder.setCartItems(cartItems);
        savedOrder.setTotal(60.0);

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        Order result = orderService.checkout(userId);

        // Assert
        assertEquals(userId, result.getUserId());
        assertEquals(60.0, result.getTotal());
        assertEquals(cartItems, result.getCartItems());
        verify(cartItemRepository, times(1)).deleteAll(cartItems);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testCheckout_EmptyCart() {
        // Arrange
        Long userId = 1L;
        when(cartItemRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.checkout(userId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Cart is empty", exception.getReason());
        verify(orderRepository, never()).save(any(Order.class));
        verify(cartItemRepository, never()).deleteAll(anyList());
    }

    @Test
    public void testGetUserOrders() {
        // Arrange
        Long userId = 1L;
        Order order = new Order();
        order.setUserId(userId);
        order.setTotal(50.0);

        List<Order> orders = Collections.singletonList(order);
        when(orderRepository.findByUserId(userId)).thenReturn(orders);

        // Act
        List<Order> result = orderService.getUserOrders(userId);

        // Assert
        assertEquals(orders, result);
        verify(orderRepository, times(1)).findByUserId(userId);
    }
}
