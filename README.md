# Spring Boot Microservices Demo
## Inter-Service Communication via Feign Client

```
┌──────────────────────────────────────────────────────────────┐
│  Client (curl / browser)                                     │
│       │                                                      │
│       │ GET /orders/{id}                                     │
│       ▼                                                      │
│  ┌─────────────────────┐   Feign Client   ┌────────────────  │
│  │   Order Service     │ ──────────────▶  │ Product Service  │
│  │   localhost:8080    │ GET /products/id │ localhost:8081   │
│  └─────────────────────┘ ◀──────────────  └─────────────────┘│
│       │  (enriched OrderDTO with ProductDTO embedded)        │
│       ▼                                                      │
│    JSON response                                             │
└──────────────────────────────────────────────────────────────┘

──▶ 2 service giao tiếp với nhau bằng cách gọi HTTP đến URL của nhau, giống hệt cách bạn dùng fetch() hay curl. 
Feign chỉ là công cụ giúp viết ít code hơn.
```

---

## Project Structure

```
microservices-demo/
├── order-service/          ← Port 8080
│   └── src/main/java/com/demo/order/
│       ├── OrderServiceApplication.java   (@EnableFeignClients)
│       ├── controller/OrderController.java
│       ├── service/OrderService.java
│       ├── client/ProductClient.java      (Feign interface)
│       └── dto/{OrderDTO, ProductDTO}.java
│
└── product-service/        ← Port 8081
    └── src/main/java/com/demo/product/
        ├── ProductServiceApplication.java
        ├── controller/ProductController.java
        ├── service/ProductService.java
        └── dto/ProductDTO.java
```

---

## How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Step 1 — Start Product Service first
```bash
cd product-service
mvn spring-boot:run
# Listening on http://localhost:8081
```

### Step 2 — Start Order Service
```bash
cd order-service
mvn spring-boot:run
# Listening on http://localhost:8080
```

---

## Example Requests

### Direct call to Product Service
```bash
curl http://localhost:8081/products/1
```
```json
{
  "id": 1,
  "name": "Wireless Headphones",
  "category": "Electronics",
  "price": 79.99,
  "stock": 150,
  "description": "Noise-cancelling over-ear headphones with 30h battery"
}
```

### Call Order Service (triggers inter-service call automatically)
```bash
curl http://localhost:8080/orders/1
```
```json
{
  "orderId": 1,
  "customerName": "Alice Martin",
  "status": "CONFIRMED",
  "quantity": 2,
  "product": {
    "id": 3,
    "name": "Standing Desk",
    "category": "Furniture",
    "price": 349.99,
    "stock": 30,
    "description": "Height-adjustable electric standing desk, 160x80cm"
  }
}
```

| Order ID | Customer     | Status    | Product              |
|----------|--------------|-----------|----------------------|
| 1        | Alice Martin | CONFIRMED | Standing Desk        |
| 2        | Bob Chen     | SHIPPED   | Wireless Headphones  |
| 3        | Carol White  | PENDING   | Ergonomic Chair      |
| 4        | David Kim    | DELIVERED | Mechanical Keyboard  |

---

## Key Concepts

### Feign Client (`ProductClient.java`)
```java
@FeignClient(name = "product-service", url = "${product.service.url}")
public interface ProductClient {
    @GetMapping("/products/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);
}
```
Spring generates the HTTP proxy at startup. No boilerplate `RestTemplate` code needed.

### Graceful Degradation (`OrderService.java`)
If Product Service is down, Order Service still returns order data — just without the product field. No cascading failure.

### Configuration (`application.properties`)
```properties
# order-service
product.service.url=http://localhost:8081
```
Change this single value to point at a different host (e.g. Docker service name, K8s DNS).

---

## Extending This Demo

| Goal | How |
|------|-----|
| Service discovery | Add Eureka (`spring-cloud-starter-netflix-eureka-*`) |
| Resilience | Add Resilience4j circuit breaker around Feign calls |
| Containerise | Add `Dockerfile` per service + `docker-compose.yml` |
| Tracing | Add Micrometer + Zipkin for distributed tracing |
| Config server | Externalise URLs with Spring Cloud Config |
