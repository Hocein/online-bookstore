package com.bnp.bookstore.controller;

import com.bnp.bookstore.entity.CartItem;
import com.bnp.bookstore.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Add item to cart
    @PostMapping("/add")
    public ResponseEntity<CartItem> addCartItem(@Valid @RequestBody CartItem cartItem) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = (Long) authentication.getDetails(); // Get the user ID from authentication details
            CartItem savedItem = cartService.addCartItem(cartItem, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Get all cart items for the authenticated user
    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = (Long) authentication.getDetails();
            List<CartItem> cartItems = cartService.getCartItems(userId);
            return ResponseEntity.ok(cartItems);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Update cart item
    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(
            @PathVariable Long cartItemId, @Valid @RequestBody CartItem cartItem) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = (Long) authentication.getDetails();
            CartItem updatedItem = cartService.updateCartItem(cartItemId, cartItem, userId);
            return ResponseEntity.ok(updatedItem);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Delete cart item
    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = (Long) authentication.getDetails();
            cartService.deleteCartItem(cartItemId, userId);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
