package com.demo.order.service;

import com.demo.order.client.ProductClient;
import com.demo.order.dto.OrderDTO;
import com.demo.order.dto.ProductDTO;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final ProductClient productClient;

    // Hardcoded orders: orderId → (orderDetails, productId)
    private static final Map<Long, long[]> ORDER_STORE = new HashMap<>();   // value: [productId, quantity]
    private static final Map<Long, OrderDTO> ORDER_META  = new HashMap<>();

    static {
        ORDER_META.put(1L, new OrderDTO(1L, "Alice Martin",  "CONFIRMED", 2));
        ORDER_META.put(2L, new OrderDTO(2L, "Bob Chen",      "SHIPPED",   1));
        ORDER_META.put(3L, new OrderDTO(3L, "Carol White",   "PENDING",   3));
        ORDER_META.put(4L, new OrderDTO(4L, "David Kim",     "DELIVERED", 1));

        // map orderId → productId
        ORDER_STORE.put(1L, new long[]{3L}); // Alice ordered a Standing Desk
        ORDER_STORE.put(2L, new long[]{1L}); // Bob   ordered Wireless Headphones
        ORDER_STORE.put(3L, new long[]{4L}); // Carol ordered an Ergonomic Chair
        ORDER_STORE.put(4L, new long[]{2L}); // David ordered a Mechanical Keyboard
    }

    public OrderService(ProductClient productClient) {
        this.productClient = productClient;
    }

    /**
     * Fetch an order by ID and enrich it with product data fetched from Product Service.
     */
    public Optional<OrderDTO> getOrderWithProduct(Long orderId) {
        OrderDTO order = ORDER_META.get(orderId);
        if (order == null) return Optional.empty();

        long productId = ORDER_STORE.get(orderId)[0];

        try {
            log.info("Order Service → calling Product Service for productId={}", productId);
            ProductDTO product = productClient.getProductById(productId);
            order.setProduct(product);
            log.info("Product Service responded: {}", product.getName());
        } catch (FeignException.NotFound e) {
            log.warn("Product {} not found in Product Service", productId);
        } catch (Exception e) {
            log.error("Failed to reach Product Service: {}", e.getMessage());
            // Graceful degradation — return order without product details
        }

        return Optional.of(order);
    }
}
