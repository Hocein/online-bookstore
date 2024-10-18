package com.bnp.bookstore.services;

import com.bnp.bookstore.entity.Order;
;
import java.util.List;

public interface OrderService {
    Order checkout(Long userId);
    List<Order> getUserOrders(Long userId);
}
