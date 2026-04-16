package com.demo.product.service;

import com.demo.product.dto.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    // Hardcoded product catalog — no database required
    private static final Map<Long, ProductDTO> PRODUCT_CATALOG = new HashMap<>();

    static {
        PRODUCT_CATALOG.put(
                1L,
                new ProductDTO(1L, "Wireless Headphones",  "Electronics", 79.99,  150, "Noise-cancelling over-ear headphones with 30h battery")
        );
        PRODUCT_CATALOG.put(
                2L,
                new ProductDTO(2L, "Mechanical Keyboard",  "Electronics", 129.99,  75, "TKL layout, Cherry MX switches, RGB backlight")
        );
        PRODUCT_CATALOG.put(
                3L,
                new ProductDTO(3L, "Standing Desk",        "Furniture",   349.99,  30, "Height-adjustable electric standing desk, 160x80cm")
        );
        PRODUCT_CATALOG.put(
                4L,
                new ProductDTO(4L, "Ergonomic Chair",      "Furniture",   299.99,  20, "Lumbar support, adjustable armrests, mesh back")
        );
        PRODUCT_CATALOG.put(
                5L,
                new ProductDTO(5L, "USB-C Hub",            "Accessories",  39.99, 200, "7-in-1 hub: HDMI, USB-A x3, SD card, PD charging")
        );
    }

    public Optional<ProductDTO> findById(Long id) {

        return Optional.ofNullable(PRODUCT_CATALOG.get(id));
    }
}
