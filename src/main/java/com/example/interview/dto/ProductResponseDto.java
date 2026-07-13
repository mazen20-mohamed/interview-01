package com.example.interview.dto;

public record ProductResponseDto(
    Long id,
    String title,
    double price,
    double rating,
    double discountPercentage
) {}
