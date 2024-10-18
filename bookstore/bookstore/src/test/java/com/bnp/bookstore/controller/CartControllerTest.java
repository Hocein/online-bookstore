package com.bnp.bookstore.controller;

import com.bnp.bookstore.entity.CartItem;
import com.bnp.bookstore.services.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testAddCartItem_Success() {
        // Arrange
        CartItem cartItem = new CartItem();
        Long userId = 1L;
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getDetails()).thenReturn(userId);
        when(cartService.addCartItem(any(CartItem.class), eq(userId))).thenReturn(cartItem);

        // Act
        ResponseEntity<?> response = cartController.addCartItem(cartItem);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cartItem, response.getBody());
    }

    @Test
    public void testAddCartItem_Unauthorized() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(false);

        // Act
        ResponseEntity<?> response = cartController.addCartItem(new CartItem());

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testGetCartItems_Success() {
        // Arrange
        Long userId = 1L;
        List<CartItem> cartItems = Collections.singletonList(new CartItem());
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getDetails()).thenReturn(userId);
        when(cartService.getCartItems(userId)).thenReturn(cartItems);

        // Act
        ResponseEntity<?> response = cartController.getCartItems();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartItems, response.getBody());
    }

    @Test
    public void testGetCartItems_Unauthorized() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(false);

        // Act
        ResponseEntity<?> response = cartController.getCartItems();

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testUpdateCartItem_Success() {
        // Arrange
        Long userId = 1L;
        Long cartItemId = 1L;
        CartItem updatedItem = new CartItem();
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getDetails()).thenReturn(userId);
        when(cartService.updateCartItem(cartItemId, updatedItem, userId)).thenReturn(updatedItem);

        // Act
        ResponseEntity<?> response = cartController.updateCartItem(cartItemId, updatedItem);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedItem, response.getBody());
    }

    @Test
    public void testUpdateCartItem_Unauthorized() {
        // Arrange
        Long cartItemId = 1L;
        CartItem cartItem = new CartItem();
        when(authentication.isAuthenticated()).thenReturn(false);

        // Act
        ResponseEntity<?> response = cartController.updateCartItem(cartItemId, cartItem);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testDeleteCartItem_Success() {
        // Arrange
        Long userId = 1L;
        Long cartItemId = 1L;
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getDetails()).thenReturn(userId);
        doNothing().when(cartService).deleteCartItem(cartItemId, userId);

        // Act
        ResponseEntity<?> response = cartController.deleteCartItem(cartItemId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartService, times(1)).deleteCartItem(cartItemId, userId);
    }

    @Test
    public void testDeleteCartItem_Unauthorized() {
        // Arrange
        Long cartItemId = 1L;
        when(authentication.isAuthenticated()).thenReturn(false);

        // Act
        ResponseEntity<?> response = cartController.deleteCartItem(cartItemId);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
