package com.example.interview.dto;

import java.util.List;

public record SearchAndLogResponse(
    Long logId,
    String query,
    Double minPriceFilter,
    StatsDto stats,
    List<ProductResponseDto> products
) {}
