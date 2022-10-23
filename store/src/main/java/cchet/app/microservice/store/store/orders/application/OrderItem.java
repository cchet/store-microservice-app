package cchet.app.microservice.store.store.orders.application;

import java.math.BigDecimal;

public record OrderItem(String productId, int count, BigDecimal price) {
    
}
