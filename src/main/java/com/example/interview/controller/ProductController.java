package com.example.interview.controller;

import com.example.interview.dto.SearchAndLogResponse;
import com.example.interview.service.ProductService;

public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // TODO: Validate the incoming parameters and delegate to the service.
    // Rules:
    // - q is required and cannot be blank.
    // - minPrice is optional.
    // - sortBy is optional and defaults to "price".
    public SearchAndLogResponse searchAndLog(String q, Double minPrice, String sortBy) {
        return productService.searchFilterAndLog(q,minPrice,sortBy);
    }
}
