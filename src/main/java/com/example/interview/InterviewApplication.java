package com.example.interview;

import com.example.interview.client.DummyJsonProductClient;
import com.example.interview.controller.ProductController;
import com.example.interview.dto.ProductResponseDto;
import com.example.interview.dto.SearchAndLogResponse;
import com.example.interview.exception.BadRequestException;
import com.example.interview.repository.SearchQueryLogRepository;
import com.example.interview.service.ProductService;
import com.example.interview.exception.NoProductsFoundException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class InterviewApplication {

    public static void main(String[] args) throws IOException {
        ProductController controller = new ProductController(
            new ProductService(
                new DummyJsonProductClient(),
                new SearchQueryLogRepository()
            )
        );

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/products/search-and-log", exchange -> handleSearch(exchange, controller));
        server.start();

        System.out.println("Server started at http://localhost:8080");
    }

    private static void handleSearch(HttpExchange exchange, ProductController controller) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            writeResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
            return;
        }

        try {
            Map<String, String> queryParams = parseQuery(exchange.getRequestURI().getRawQuery());
            String q = queryParams.get("q");
            Double minPrice = parseOptionalDouble(queryParams.get("minPrice"));
            String sortBy = queryParams.get("sortBy");

            SearchAndLogResponse response = controller.searchAndLog(q, minPrice, sortBy);
            writeResponse(exchange, 200, toJson(response));
        } catch (IllegalArgumentException | BadRequestException ex) {
            writeResponse(exchange, 400, "{\"error\":\"" + escapeJson(ex.getMessage()) + "\"}");
        } catch (NoProductsFoundException ex) {
            writeResponse(exchange, 404, "{\"error\":\"" + escapeJson(ex.getMessage()) + "\"}");
        } catch (Exception ex) {
            writeResponse(exchange, 500, "{\"error\":\"Internal Server Error: " + escapeJson(ex.getMessage()) + "\"}");
        }
    }

    private static Map<String, String> parseQuery(String rawQuery) {
        Map<String, String> params = new HashMap<>();
        if (rawQuery == null || rawQuery.isBlank()) {
            return params;
        }

        for (String pair : rawQuery.split("&")) {
            String[] parts = pair.split("=", 2);
            String key = decode(parts[0]);
            String value = parts.length == 2 ? decode(parts[1]) : "";
            params.put(key, value);
        }

        return params;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static Double parseOptionalDouble(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Double.parseDouble(value);
    }

    private static void writeResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(bytes);
        }
    }

    private static String toJson(SearchAndLogResponse response) {
        String productsJson = response.products().stream()
            .map(InterviewApplication::toJson)
            .collect(Collectors.joining(","));

        return "{"
            + "\"logId\":" + response.logId()
            + ",\"query\":\"" + escapeJson(response.query()) + "\""
            + ",\"minPriceFilter\":" + nullableNumber(response.minPriceFilter())
            + ",\"stats\":{"
            + "\"averagePrice\":" + response.stats().averagePrice()
            + ",\"maxDiscount\":" + response.stats().maxDiscount()
            + "}"
            + ",\"products\":[" + productsJson + "]"
            + "}";
    }

    private static String toJson(ProductResponseDto product) {
        return "{"
            + "\"id\":" + product.id()
            + ",\"title\":\"" + escapeJson(product.title()) + "\""
            + ",\"price\":" + product.price()
            + ",\"rating\":" + product.rating()
            + ",\"discountPercentage\":" + product.discountPercentage()
            + "}";
    }

    private static String nullableNumber(Double value) {
        return value == null ? "null" : value.toString();
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"");
    }
}
