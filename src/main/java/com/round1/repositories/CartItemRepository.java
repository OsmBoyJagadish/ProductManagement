package com.round1.repositories;

import com.round1.entities.Cart;
import com.round1.entities.CartItem;
import com.round1.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}