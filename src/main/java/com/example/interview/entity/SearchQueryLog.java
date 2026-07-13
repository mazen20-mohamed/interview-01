package com.example.interview.entity;

import java.time.LocalDateTime;

public class SearchQueryLog {

    private Long id;
    private String queryText;
    private Double minPriceFilter;
    private Integer matchedCount;
    private LocalDateTime searchedAt;

    public SearchQueryLog(String queryText, Double minPriceFilter, Integer matchedCount, LocalDateTime searchedAt) {
        this.queryText = queryText;
        this.minPriceFilter = minPriceFilter;
        this.matchedCount = matchedCount;
        this.searchedAt = searchedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQueryText() { return queryText; }
    public void setQueryText(String queryText) { this.queryText = queryText; }

    public Double getMinPriceFilter() { return minPriceFilter; }
    public void setMinPriceFilter(Double minPriceFilter) { this.minPriceFilter = minPriceFilter; }

    public Integer getMatchedCount() { return matchedCount; }
    public void setMatchedCount(Integer matchedCount) { this.matchedCount = matchedCount; }

    public LocalDateTime getSearchedAt() { return searchedAt; }
    public void setSearchedAt(LocalDateTime searchedAt) { this.searchedAt = searchedAt; }
}
