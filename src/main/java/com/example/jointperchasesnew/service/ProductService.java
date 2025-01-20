package com.example.jointperchasesnew.service;

import com.example.jointperchasesnew.dto.ProductDto;
import com.example.jointperchasesnew.model.entity.Product;
import com.example.jointperchasesnew.representation.ProductRepresentation;
import org.springframework.hateoas.CollectionModel;

public interface ProductService {
    public ProductRepresentation createProduct(ProductDto productDto);
    public Product getProductDataForCreate(ProductDto productDto);
    public ProductRepresentation getProduct(String productName);
    public Product getProductData(String productName);
    public CollectionModel<ProductRepresentation> getAllProducts();
}
