package com.bnp.bookstore.repository;

import com.bnp.bookstore.entity.CartItem;
import com.bnp.bookstore.entity.Order;
import com.bnp.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves all orders placed by a given user.
     *
     * @param userId associated user.
     * @return list of orders associated to provided user.
     */
    List<Order> findByUserId(Long userId);
}
