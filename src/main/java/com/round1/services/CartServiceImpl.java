package com.round1.services;

import com.round1.dto.request.CartItemRequest;
import com.round1.dto.response.CartItemResponse;
import com.round1.dto.response.CartResponse;
import com.round1.entities.Cart;
import com.round1.entities.CartItem;
import com.round1.entities.Product;
import com.round1.entities.User;
import com.round1.enums.ProductStatus;
import com.round1.exception.ProductNotActiveException;
import com.round1.exception.ResourceNotFoundException;
import com.round1.repositories.CartItemRepository;
import com.round1.repositories.CartRepository;
import com.round1.repositories.ProductRepository;
import com.round1.repositories.UserRepository;
import com.round1.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(String email) {
        Cart cart = getCartByEmail(email);
        return toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItemToCart(String email, CartItemRequest request) {
        Cart cart = getCartByEmail(email);
        Product product = getActiveProduct(request.getProductId());

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            cart.getCartItems().add(cartItemRepository.save(newItem));
        }

        return toResponse(cartRepository.findById(cart.getId()).orElseThrow());
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(String email, Long cartItemId, CartItemRequest request) {
        Cart cart = getCartByEmail(email);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new ResourceNotFoundException("CartItem", cartItemId);
        }

        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);

        return toResponse(cartRepository.findById(cart.getId()).orElseThrow());
    }

    @Override
    @Transactional
    public CartResponse removeCartItem(String email, Long cartItemId) {
        Cart cart = getCartByEmail(email);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new ResourceNotFoundException("CartItem", cartItemId);
        }

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        return toResponse(cartRepository.findById(cart.getId()).orElseThrow());
    }

    @Override
    @Transactional
    public void clearCart(String email) {
        Cart cart = getCartByEmail(email);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    private Cart getCartByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + email));
    }

    private Product getActiveProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));
        if (product.getStatus() == ProductStatus.INACTIVE) {
            throw new ProductNotActiveException(product.getName());
        }
        return product;
    }

    private CartResponse toResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getCartItems().stream()
                .map(item -> CartItemResponse.builder()
                        .cartItemId(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .price(item.getProduct().getPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getProduct().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity())))
                        .build())
                .collect(Collectors.toList());

        BigDecimal total = itemResponses.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(itemResponses)
                .totalAmount(total)
                .build();
    }
}