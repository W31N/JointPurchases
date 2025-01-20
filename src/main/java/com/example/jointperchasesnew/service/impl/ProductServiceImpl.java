package com.example.jointperchasesnew.service.impl;

import com.example.jointperchasesnew.dto.ProductDto;
import com.example.jointperchasesnew.exception.ProductAlreadyExistException;
import com.example.jointperchasesnew.exception.ProductNotFoundException;
import com.example.jointperchasesnew.model.entity.Product;
import com.example.jointperchasesnew.repository.ProductRepository;
import com.example.jointperchasesnew.representation.ProductRepresentation;
import com.example.jointperchasesnew.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private ModelMapper modelMapper;

    public ProductServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductRepresentation createProduct(ProductDto productDto) {
        Product newProduct = getProductDataForCreate(productDto);
        ProductRepresentation productRepresentation = modelMapper.map(newProduct, ProductRepresentation.class);
        addLinks();
        return productRepresentation;
    }

    @Override
    public Product getProductDataForCreate(ProductDto productDto) {
        if (productRepository.findByName(productDto.getName()).isPresent())
            throw new ProductAlreadyExistException("Product already exist");
        Product newProduct = modelMapper.map(productDto, Product.class);
        return productRepository.saveAndFlush(newProduct);
    }

    @Override
    public ProductRepresentation getProduct(String productName) {
        Optional<Product> optionalProduct = productRepository.findByName(productName);
        if (optionalProduct.isEmpty())
            throw new ProductNotFoundException("Product not found");
        Product product = getProductData(productName);
        ProductRepresentation productRepresentation = modelMapper.map(product, ProductRepresentation.class);
        addLinks();
        return productRepresentation;
    }

    @Override
    public Product getProductData(String productName) {
        Optional<Product> optionalProduct = productRepository.findByName(productName);
        if (optionalProduct.isEmpty())
            throw new ProductNotFoundException("Product not found");
        return optionalProduct.get();
    }

    @Override
    public CollectionModel<ProductRepresentation> getAllProducts() {
        List<Product> products = productRepository.findAll();

        // Преобразуем список продуктов в список представлений
        List<ProductRepresentation> productRepresentations = products.stream()
                .map(product -> {
                    ProductRepresentation representation = modelMapper.map(product, ProductRepresentation.class);
                    addLinks(representation, product.getName());
                    return representation;
                })
                .toList();
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
