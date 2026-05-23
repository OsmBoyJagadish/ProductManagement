package com.round1.services;

import com.round1.dto.request.ProductRequest;
import com.round1.dto.request.UpdateProductRequest;
import com.round1.dto.response.ProductResponse;
import com.round1.entities.Product;
import com.round1.enums.ProductStatus;
import com.round1.exception.ResourceNotFoundException;
import com.round1.repositories.ProductRepository;
import com.round1.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductResponse addProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .status(ProductStatus.ACTIVE)
                .build();
        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long productId, UpdateProductRequest request) {
        Product product = findProductById(productId);

        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getQuantity() != null) {
            product.setQuantity(request.getQuantity());
        }

        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse enableProduct(Long productId) {
        Product product = findProductById(productId);
        product.setStatus(ProductStatus.ACTIVE);
        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse disableProduct(Long productId) {
        Product product = findProductById(productId);
        product.setStatus(ProductStatus.INACTIVE);
        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getActiveProducts() {
        return productRepository.findByStatus(ProductStatus.ACTIVE)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        return toResponse(findProductById(productId));
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .build();
    }
}