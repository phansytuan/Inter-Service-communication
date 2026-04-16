package com.demo.order.client;

import com.demo.order.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign declarative HTTP client.
 * Spring generates a proxy at startup — no boilerplate HTTP code needed.

 * url is read from application.properties via ${product.service.url}
 */
@FeignClient(name = "product-service", url = "${product.service.url}")
public interface ProductClient {

    @GetMapping("/products/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);
    // GET http://localhost:8081/products/3
}
