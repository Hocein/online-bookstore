package com.bnp.bookstore.services.impl;

import com.bnp.bookstore.entity.CartItem;
import com.bnp.bookstore.repository.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCartItem_shouldSaveAndReturnCartItem() {
        // Arrange
        Long userId = 1L;
        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        // Act
        CartItem result = cartService.addCartItem(cartItem, userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    void getCartItems_shouldReturnListOfCartItems() {
        // Arrange
        Long userId = 1L;
        List<CartItem> expectedItems = Arrays.asList(new CartItem(), new CartItem());
        when(cartItemRepository.findByUserId(userId)).thenReturn(expectedItems);

        // Act
        List<CartItem> result = cartService.getCartItems(userId);

        // Assert
        assertEquals(expectedItems, result);
        verify(cartItemRepository).findByUserId(userId);
    }

    @Test
    void updateCartItem_shouldUpdateAndReturnCartItem_whenUserOwnsItem() {
        // Arrange
        Long userId = 1L;
        Long cartItemId = 1L;
        CartItem existingItem = new CartItem();
        existingItem.setId(cartItemId);
        existingItem.setUserId(userId);
        existingItem.setQuantity(1);

        CartItem updatedItem = new CartItem();
        updatedItem.setQuantity(2);

        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(existingItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(existingItem);

        // Act
        CartItem result = cartService.updateCartItem(cartItemId, updatedItem, userId);

        // Assert
        assertEquals(2, result.getQuantity());
        verify(cartItemRepository).findById(cartItemId);
        verify(cartItemRepository).save(existingItem);
    }

    @Test
    void updateCartItem_shouldThrowException_whenUserDoesNotOwnItem() {
        // Arrange
        Long userId = 1L;
        Long cartItemId = 1L;
        CartItem existingItem = new CartItem();
        existingItem.setId(cartItemId);
        existingItem.setUserId(2L); // Different user ID

        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(existingItem));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> cartService.updateCartItem(cartItemId, new CartItem(), userId));
        verify(cartItemRepository).findById(cartItemId);
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void deleteCartItem_shouldDeleteItem_whenUserOwnsItem() {
        // Arrange
        Long userId = 1L;
        Long cartItemId = 1L;
        CartItem existingItem = new CartItem();
        existingItem.setId(cartItemId);
        existingItem.setUserId(userId);

        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(existingItem));

        // Act
        cartService.deleteCartItem(cartItemId, userId);

        // Assert
        verify(cartItemRepository).findById(cartItemId);
        verify(cartItemRepository).delete(existingItem);
    }

    @Test
    void deleteCartItem_shouldThrowException_whenUserDoesNotOwnItem() {
        // Arrange
        Long userId = 1L;
        Long cartItemId = 1L;
        CartItem existingItem = new CartItem();
        existingItem.setId(cartItemId);
        existingItem.setUserId(2L); // Different user ID

        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(existingItem));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> cartService.deleteCartItem(cartItemId, userId));
        verify(cartItemRepository).findById(cartItemId);
        verify(cartItemRepository, never()).delete(any(CartItem.class));
    }

    @Test
    void deleteCartItem_shouldThrowException_whenItemDoesNotExist() {
        // Arrange
        Long userId = 1L;
        Long cartItemId = 1L;
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> cartService.deleteCartItem(cartItemId, userId));
        verify(cartItemRepository).findById(cartItemId);
        verify(cartItemRepository, never()).delete(any(CartItem.class));
    }
}