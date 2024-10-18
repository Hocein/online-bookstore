package com.bnp.bookstore.repository;

import com.bnp.bookstore.entity.CartItem;
import com.bnp.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Get all cart items by provided user.
     *
     * @param user provided user.
     * @return Une list of cart items.
     */
    List<CartItem> findByUserId(Long userId);
}
