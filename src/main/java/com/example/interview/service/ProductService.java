package com.example.interview.service;

import com.example.interview.client.DummyJsonProductClient;
import com.example.interview.client.dto.DummyProductDto;
import com.example.interview.dto.ProductResponseDto;
import com.example.interview.dto.SearchAndLogResponse;
import com.example.interview.dto.StatsDto;
import com.example.interview.entity.SearchQueryLog;
import com.example.interview.exception.BadRequestException;
import com.example.interview.exception.NoProductsFoundException;
import com.example.interview.repository.SearchQueryLogRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.DoubleAdder;

public class ProductService {

    private final DummyJsonProductClient productClient;
    private final SearchQueryLogRepository queryLogRepository;

    public ProductService(DummyJsonProductClient productClient, SearchQueryLogRepository queryLogRepository) {
        this.productClient = productClient;
        this.queryLogRepository = queryLogRepository;
    }

    public SearchAndLogResponse searchFilterAndLog(String query, Double minPrice, String sortBy) {

        if(query.isEmpty()){
            throw new BadRequestException("Query Param is missing.");
        }

        Map<String, Comparator<ProductResponseDto>> comparators = Map.of(
                "price", Comparator.comparing(ProductResponseDto::price),
                "rating", Comparator.comparing(ProductResponseDto::rating),
                "discount", Comparator.comparing(ProductResponseDto::discountPercentage)
        );

        if(comparators.containsKey(sortBy)){
            throw new BadRequestException("Invalid sortBy value");
        }
        if(minPrice < 0){
            throw new BadRequestException("minPrice cannot be less than 0");
        }



        AtomicReference<Double> maxDiscount = new AtomicReference<>(Double.MIN_VALUE);
        DoubleAdder totalPrice = new DoubleAdder();
        AtomicInteger count = new AtomicInteger();



        Comparator<ProductResponseDto> comparator = comparators.get(sortBy.toLowerCase());

        List<ProductResponseDto> products = productClient.searchProducts(query)
                .products()
                .stream()
                .filter(product -> product.price() >= minPrice)
                .peek(product -> {
                    if (maxDiscount.get() < product.discountPercentage()) {
                        maxDiscount.set(product.discountPercentage());
                    }

                    totalPrice.add(product.price());
                    count.incrementAndGet();
                })
                .map(product -> new ProductResponseDto(product.id()
                        ,product.title(),product.price(),product.rating(),product.discountPercentage()))
                .sorted(comparator.reversed())
                .toList();

        if(products.isEmpty()){
            throw new NoProductsFoundException("No products found with the required range");
        }


        SearchQueryLog searchQueryLog = new SearchQueryLog(query,minPrice,products.size(), LocalDateTime.now());

        searchQueryLog = queryLogRepository.save(searchQueryLog);


        StatsDto stats = new StatsDto(
                totalPrice.sum() / count.get(),
                maxDiscount.get()
        );

        return new SearchAndLogResponse(searchQueryLog.getId(),query,minPrice,stats,products);
    }
}
