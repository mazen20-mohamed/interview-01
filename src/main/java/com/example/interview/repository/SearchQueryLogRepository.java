package com.example.interview.repository;

import com.example.interview.entity.SearchQueryLog;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class SearchQueryLogRepository {

    private final AtomicLong sequence = new AtomicLong(1);
    private final List<SearchQueryLog> logs = new CopyOnWriteArrayList<>();

    public SearchQueryLog save(SearchQueryLog log) {
        log.setId(sequence.getAndIncrement());
        logs.add(log);
        return log;
    }

    public List<SearchQueryLog> findAll() {
        return List.copyOf(logs);
    }
}
