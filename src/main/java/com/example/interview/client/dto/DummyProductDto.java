package com.example.interview.client.dto;

public record DummyProductDto(
    Long id,
    String title,
    double price,
    double rating,
    double discountPercentage
) {}
