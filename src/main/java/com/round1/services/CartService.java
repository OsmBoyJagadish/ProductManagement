package com.round1.services;

import com.round1.dto.request.CartItemRequest;
import com.round1.dto.response.CartResponse;

public interface CartService {
    CartResponse getCart(String email);
    CartResponse addItemToCart(String email, CartItemRequest request);
    CartResponse updateCartItem(String email, Long cartItemId, CartItemRequest request);
    CartResponse removeCartItem(String email, Long cartItemId);
    void clearCart(String email);
}