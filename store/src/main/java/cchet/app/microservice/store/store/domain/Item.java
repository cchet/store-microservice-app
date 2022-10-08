package cchet.app.microservice.store.store.domain;

import java.math.BigDecimal;

public record Item(String productId, int count, BigDecimal price) {
    
}
