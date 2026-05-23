package com.round1.services;

import com.round1.dto.request.ProductRequest;
import com.round1.dto.request.UpdateProductRequest;
import com.round1.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse addProduct(ProductRequest request);
    ProductResponse updateProduct(Long productId, UpdateProductRequest request);
    ProductResponse enableProduct(Long productId);
    ProductResponse disableProduct(Long productId);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getActiveProducts();
    ProductResponse getProductById(Long productId);
}