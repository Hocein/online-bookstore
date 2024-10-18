package com.bnp.bookstore.services.impl;

import com.bnp.bookstore.entity.CartItem;
import com.bnp.bookstore.repository.CartItemRepository;
import com.bnp.bookstore.repository.UserRepository;
import com.bnp.bookstore.services.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    public CartServiceImpl(CartItemRepository cartItemRepository, UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CartItem addCartItem(CartItem cartItem, Long userId) {
        cartItem.setUserId(userId);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public List<CartItem> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    public CartItem updateCartItem(Long cartItemId, CartItem cartItem, Long userId) {
        CartItem existingItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CartItem not found"));

        validateOwnership(existingItem, userId);

        existingItem.setQuantity(cartItem.getQuantity());
        return cartItemRepository.save(existingItem);
    }

    @Override
    public void deleteCartItem(Long cartItemId, Long userId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CartItem not found"));

        validateOwnership(cartItem, userId);
        cartItemRepository.delete(cartItem);
    }


    private void validateOwnership(CartItem cartItem, Long userId) {
        if (!cartItem.getUserId().equals(userId)) {
            logger.warn("Unauthorized action attempted by user: {}", userId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized action");
        }
    }
}
