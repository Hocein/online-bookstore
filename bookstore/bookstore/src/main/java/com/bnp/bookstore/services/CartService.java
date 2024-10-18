package com.bnp.bookstore.services;

import com.bnp.bookstore.entity.CartItem;

import java.util.List;

public interface CartService {
    CartItem addCartItem(CartItem cartItem, Long userId);
    List<CartItem> getCartItems(Long userId);
    CartItem updateCartItem(Long cartItemId, CartItem cartItem, Long userId);
    void deleteCartItem(Long cartItemId, Long userId);
}
