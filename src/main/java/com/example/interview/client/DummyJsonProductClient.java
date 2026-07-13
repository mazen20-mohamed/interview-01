package com.example.interview.client;

import com.example.interview.client.dto.DummyProductDto;
import com.example.interview.client.dto.DummyProductListDto;

import java.util.List;
import java.util.Locale;

public class DummyJsonProductClient {

    private static final List<DummyProductDto> PRODUCTS = List.of(
        new DummyProductDto(1L, "Apple iPhone", 999.0, 4.8, 12.5),
        new DummyProductDto(2L, "Apple Watch", 399.0, 4.6, 8.0),
        new DummyProductDto(3L, "Samsung Phone", 799.0, 4.5, 10.0),
        new DummyProductDto(4L, "Sony Headphones", 199.0, 4.3, 18.0),
        new DummyProductDto(5L, "Dell Laptop", 1299.0, 4.4, 15.0)
    );

    public DummyProductListDto searchProducts(String query) {
        String normalizedQuery = query == null ? "" : query.toLowerCase(Locale.ROOT);

        List<DummyProductDto> matches = PRODUCTS.stream()
            .filter(product -> product.title().toLowerCase(Locale.ROOT).contains(normalizedQuery))
            .toList();

        return new DummyProductListDto(matches);
    }
}
