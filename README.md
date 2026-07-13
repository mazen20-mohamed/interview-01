# Java Backend Coding Challenge: Product Filter, Aggregator & Logger API

This is a lightweight Java backend coding challenge for a 20-minute interview.

The goal is to evaluate basic backend structure and implementation skills:

- Controller: receive and validate request parameters.
- Service: implement business logic.
- DTO: shape input/output data.
- DAO/Repository: save search metadata.
- Client: fetch product data from an already provided source.

This project intentionally does not use Spring Boot, JPA, H2, Lombok, or external HTTP clients. The challenge should test Java/backend fundamentals, not framework setup.

## Challenge Specification

Detailed API behavior, response format, and evaluation criteria are available here:

[Java Backend Coding Challenge Specification](https://wiki.david888.com/share/7d186b781ebbfc22ff79614ea049ab66)

## Environment

Choose one option:

- Local: Java 17 or newer.
- Docker: Docker only. Use this path when the local Java setup is unavailable or uncertain.

The included Maven Wrapper (`./mvnw`) downloads Maven automatically when needed. The project compiles to Java 17 bytecode for portability.

## Project Structure

The starter project already includes:

- `ProductController`: request parameter validation and service delegation.
- `ProductService`: product filtering, sorting, aggregation, and logging logic.
- `SearchAndLogResponse`, `ProductResponseDto`, `StatsDto`: response DTOs.
- `SearchQueryLogRepository`: in-memory DAO for saved search logs.
- `DummyJsonProductClient`: provided in-memory product source.
- `InterviewApplication`: small JDK HTTP server for manual endpoint testing.

## Your Tasks

Implement the missing logic marked with `TODO`.

In `com.example.interview.controller.ProductController`:

- Require `q`.
- Reject blank `q`.
- Default `sortBy` to `"price"` when omitted.
- Delegate to `ProductService`.

In `com.example.interview.service.ProductService`:

- Fetch product data from `DummyJsonProductClient`.
- Filter out products where `price < minPrice` when `minPrice` is provided.
- Throw `NoProductsFoundException` when no products match.
- Compute `averagePrice` and `maxDiscount`.
- Sort products descending by `price`, `rating`, or `discount`.
- Save search metadata through `SearchQueryLogRepository`.
- Return `SearchAndLogResponse`.

## Run Locally

```bash
./mvnw package
java -jar target/interview-java-challenge-0.0.1-SNAPSHOT.jar
```

After completing the TODOs, test the endpoint:

```bash
curl -i "http://localhost:8080/api/products/search-and-log?q=apple&minPrice=100&sortBy=price"
```

## Run with Docker

```bash
docker build -t interview-java-challenge .
docker run --rm -p 8080:8080 interview-java-challenge
```

After completing the TODOs, test the endpoint:

```bash
curl -i "http://localhost:8080/api/products/search-and-log?q=apple&minPrice=100&sortBy=price"
```

---

# Java Backend 上機實作題：Product Filter, Aggregator & Logger API

這是一份輕量級 Java backend 上機實作題，設計目標是 20 分鐘內完成。

本題要評估的是候選人的基礎 backend 分層與實作能力：

- Controller：接收並驗證 request 參數。
- Service：實作商業邏輯。
- DTO：定義輸入與輸出資料格式。
- DAO/Repository：儲存搜尋紀錄。
- Client：從已提供的資料來源取得產品資料。

本專案刻意不使用 Spring Boot、JPA、H2、Lombok 或外部 HTTP client。這題應該測 Java/backend 基礎，不應該測框架與環境設定。

## 題目規格

詳細 API 行為、回傳格式與評分標準請參考：

[Java Backend Coding Challenge Specification](https://wiki.david888.com/share/7d186b781ebbfc22ff79614ea049ab66)

## 執行環境

請擇一使用：

- 本機執行：Java 17 或更新版本。
- Docker 執行：只需要 Docker。若本機 Java 環境不確定，請直接使用此方式。

專案已包含 Maven Wrapper (`./mvnw`)，需要時會自動下載 Maven。本專案會編譯成 Java 17 bytecode，以提高可攜性。

## 專案結構

本專案已提供：

- `ProductController`：request 參數驗證與 service 委派。
- `ProductService`：產品過濾、排序、統計與寫入紀錄。
- `SearchAndLogResponse`、`ProductResponseDto`、`StatsDto`：回傳 DTO。
- `SearchQueryLogRepository`：in-memory DAO，用來保存搜尋紀錄。
- `DummyJsonProductClient`：已提供的 in-memory 產品資料來源。
- `InterviewApplication`：使用 JDK 內建 HTTP server，方便手動測試 endpoint。

## 實作任務

請完成程式中標記 `TODO` 的邏輯。

在 `com.example.interview.controller.ProductController`：

- `q` 為必填。
- 拒絕空白的 `q`。
- `sortBy` 未提供時預設為 `"price"`。
- 委派給 `ProductService`。

在 `com.example.interview.service.ProductService`：

- 從 `DummyJsonProductClient` 取得產品資料。
- 當有提供 `minPrice` 時，排除 `price < minPrice` 的產品。
- 沒有任何符合產品時，拋出 `NoProductsFoundException`。
- 計算 `averagePrice` 與 `maxDiscount`。
- 依照 `price`、`rating` 或 `discount` 做降序排序。
- 透過 `SearchQueryLogRepository` 儲存搜尋紀錄。
- 回傳 `SearchAndLogResponse`。

## 本機執行

```bash
./mvnw package
java -jar target/interview-java-challenge-0.0.1-SNAPSHOT.jar
```

完成 TODO 後，測試 endpoint：

```bash
curl -i "http://localhost:8080/api/products/search-and-log?q=apple&minPrice=100&sortBy=price"
```

## Docker 執行

```bash
docker build -t interview-java-challenge .
docker run --rm -p 8080:8080 interview-java-challenge
```

完成 TODO 後，測試 endpoint：

```bash
curl -i "http://localhost:8080/api/products/search-and-log?q=apple&minPrice=100&sortBy=price"
```
